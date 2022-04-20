package com.manydesigns.orientdbmanager;

import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.manydesigns.orientdbmanager.models.TupleEl;
import com.manydesigns.orientdbmanager.utils.Utils;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static com.manydesigns.orientdbmanager.Constants.*;

/**
 * Author: Emanuele Collura
 * Date: 22/03/22
 * Time: 17:46
 */
@Slf4j
public class SetupDB {
    private static final Map<String, CompilationUnit> parsedFiles = new HashMap<>();
    private static OrientDB orient = null;
    private static ODatabaseSession db = null;
    private static final Map<Integer, OVertex> nodeAdding = new HashMap<>();
    private static final Map<String, OVertex> conditionalNode = new HashMap<>();

    public static void main(String[] args) {
        try {
            orient = new OrientDB("remote:localhost", USER_DB, PW_DB, OrientDBConfig.defaultConfig());

            if (orient.exists(NAME_DB)) {
                orient.drop(NAME_DB);
            }

            orient.create(NAME_DB, ODatabaseType.PLOCAL);

            db = orient.open(NAME_DB, USER_DB, PW_DB);
            setupDB(db);

            assert JSON_RESULT != null;
            var result = Utils.readResult(JSON_RESULT);


            for (var tuple :
                    result.getNodes().getTuples()) {
                for (var el : tuple) {
                    Integer elId = el.getId();
                    if (elId != null && !nodeAdding.containsKey(elId)) {
                        nodeAdding.put(elId, createNode(db, el));
                    }
                }
            }

            for (var tuple : result.getEdges().getTuples()) {
                TupleEl firstNode = tuple.get(0);
                TupleEl secondNode = tuple.get(1);

                var node0 = nodeAdding.get(firstNode.getId());
                var node1 = nodeAdding.get(secondNode.getId());

                createEdge(node0, node1);

/*
                var firstNodeFile = getParsedFile(firstNode.getUrl().getUri());
                var secondNodeFile = getParsedFile(secondNode.getUrl().getUri());

                List<OVertex> nodes = new ArrayList<>();

                nodes.add(node0);
                nodes.add(node1);

                var conditionalNode0 = ifNode(firstNodeFile, node0);
                if (conditionalNode0 != null)
                    nodes.add(conditionalNode0);

                var conditionalNode1 = ifNode(secondNodeFile, node1);
                if (conditionalNode1 != null)
                    nodes.add(conditionalNode1);

                nodes.sort((a, b) -> {
                    var lineA = (Integer) getNodeProperty(a, NodeVProperty.START_LINE);
                    var lineB = (Integer) getNodeProperty(b, NodeVProperty.START_LINE);
                    return lineB.compareTo(lineA);
                });

                for (var i = 1; i < nodes.size(); i++) {
                    createEdge(nodes.get(i - 1), nodes.get(i));
                }*/

            }

        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (db != null)
                db.close();
            if (orient != null)
                orient.close();
        }
    }

    private static OVertex ifNode(CompilationUnit compilationUnit, OVertex node) {
        for (var ifstmt :
                compilationUnit.findAll(IfStmt.class)) {
            var condition = ifstmt.getCondition();
            Optional<Position> ifstmtBegin = ifstmt.getBegin();
            assert ifstmtBegin.isPresent();
            var lineIf = ifstmtBegin.get().line;

            Statement thenStmt = ifstmt.getThenStmt();
            Optional<Position> thenStmtBegin = thenStmt.getBegin();
            assert thenStmtBegin.isPresent();
            int thenStmtBeginLine = thenStmtBegin.get().line;

            Optional<Position> thenStmtEnd = thenStmt.getEnd();
            assert thenStmtEnd.isPresent();
            int thenStmtEndLine = thenStmtEnd.get().line;

            var lineNode = (Integer) getNodeProperty(node, NodeVProperty.START_LINE);
            var startColumnNode = (Integer) getNodeProperty(node, NodeVProperty.START_COLUMN);

            if (startColumnNode >= condition.getBegin().get().column && startColumnNode < condition.getEnd().get().column) {
                return null;
            }

            if ((lineNode > thenStmtBeginLine && lineNode < thenStmtEndLine)) {
                String idConditionalNode = getNodeProperty(node, NodeVProperty.FILE) + " - " + lineIf;
                return conditionalNode.computeIfAbsent(idConditionalNode, k -> createConditionalNode(db, k, condition.toString(), lineIf));
            }
        }
        return null;
    }

    private static CompilationUnit getParsedFile(String url) {
        return parsedFiles.computeIfAbsent(url, k -> {
            try {
                return StaticJavaParser.parse(new File(k.substring(5)));
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException", e);
                return null;
            }
        });
    }

    private static void createEdge(OVertex node0, OVertex node1) {
        var hasEdge = false;
        for (var vertice :
                node0.getVertices(ODirection.OUT)) {
            if (getNodeProperty(vertice, NodeVProperty.ID) == getNodeProperty(node1, NodeVProperty.ID)) {
                hasEdge = true;
                break;
            }
        }
        if (!hasEdge) {
            OEdge edge1 = node0.addEdge(node1, EDGE_KEY);
            edge1.save();
        }
    }

    private static void setupDB(ODatabaseSession db) {
        OClass node = db.getClass(NODE_KEY);

        if (node == null) {
            node = db.createVertexClass(NODE_KEY);
        }

        setupPropertyNode(node, NodeVProperty.ID);
        setupPropertyNode(node, NodeVProperty.LABEL);
        setupPropertyNode(node, NodeVProperty.FILE);
        setupPropertyNode(node, NodeVProperty.START_LINE);
        setupPropertyNode(node, NodeVProperty.END_LINE);
        setupPropertyNode(node, NodeVProperty.START_COLUMN);
        setupPropertyNode(node, NodeVProperty.END_COLUMN);

        OClass conditionalNode = db.getClass(CONDITIONAL_NODE_KEY);

        if (conditionalNode == null) {
            conditionalNode = db.createVertexClass(CONDITIONAL_NODE_KEY);
        }
        setupPropertyNode(conditionalNode, ConditionalNodeVProperty.ID);
        setupPropertyNode(conditionalNode, ConditionalNodeVProperty.CONDITION);

        if (db.getClass(EDGE_KEY) == null) {
            db.createEdgeClass(EDGE_KEY);
        }
    }

    private static void setupPropertyNode(OClass node, VProperty vProperty) {
        var key = vProperty.getName();
        if (node.getProperty(key) == null) {
            node.createProperty(key, vProperty.getType());

            var indexType = OClass.INDEX_TYPE.NOTUNIQUE;
            if (vProperty.isUnique())
                indexType = OClass.INDEX_TYPE.UNIQUE;

            node.createIndex(node.getName() + "_" + key, indexType, key);
        }
    }


    private static OVertex createNode(ODatabaseSession db, TupleEl el) {
        OVertex result = db.newVertex(NODE_KEY);
        setNodeProperty(result, NodeVProperty.ID, el.getId());
        setNodeProperty(result, NodeVProperty.LABEL, el.getLabel());
        var url = el.getUrl();
        setNodeProperty(result, NodeVProperty.FILE, url.getUri().replace(COMMON_PATH, ""));
        setNodeProperty(result, NodeVProperty.START_LINE, url.getStartLine());
        setNodeProperty(result, NodeVProperty.END_LINE, url.getEndLine());
        setNodeProperty(result, NodeVProperty.START_COLUMN, url.getStartColumn());
        setNodeProperty(result, NodeVProperty.END_COLUMN, url.getEndColumn());
        result.save();
        return result;
    }

    private static OVertex createConditionalNode(ODatabaseSession db, String id, String condition, Integer line) {
        OVertex result = db.newVertex(CONDITIONAL_NODE_KEY);
        setNodeProperty(result, ConditionalNodeVProperty.ID, id);
        setNodeProperty(result, ConditionalNodeVProperty.CONDITION, condition);
        setNodeProperty(result, ConditionalNodeVProperty.LINE, line);
        result.save();
        return result;
    }

    private static void setNodeProperty(OVertex oVertex, VProperty VProperty, Object value) {
        oVertex.setProperty(VProperty.getName(), value);
    }

    private static Object getNodeProperty(OVertex oVertex, VProperty VProperty) {
        return oVertex.getProperty(VProperty.getName());
    }

}
