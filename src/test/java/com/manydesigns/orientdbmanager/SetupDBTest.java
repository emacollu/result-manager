package com.manydesigns.orientdbmanager;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

/**
 * Author: Emanuele Collura
 * Date: 22/03/22
 * Time: 17:46
 */
public class SetupDBTest {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", "root", "rootpwd", OrientDBConfig.defaultConfig());

        if (!orient.exists("test_db")) {
            orient.create("test_db", ODatabaseType.PLOCAL);
        }

        ODatabaseSession db = orient.open("test_db", "root", "rootpwd");
        OClass node = db.getClass("Node");

        if (node == null) {
            node = db.createVertexClass("Node");
        }

        if (node.getProperty("index") == null) {
            node.createProperty("index", OType.INTEGER);
            node.createIndex("Node_index", OClass.INDEX_TYPE.NOTUNIQUE, "index");
        }

        OClass nodeClass2 = db.getClass("DecisionNode");

        if (nodeClass2 == null) {
            nodeClass2 = db.createVertexClass("DecisionNode");
        }

        if (nodeClass2.getProperty("index2") == null) {
            nodeClass2.createProperty("index2", OType.INTEGER);
            nodeClass2.createIndex("Node2_index", OClass.INDEX_TYPE.NOTUNIQUE, "index2");
        }

        if (db.getClass("Edge") == null) {
            db.createEdgeClass("Edge");
        }

        OVertex node0 = createNode(db, 0);
        OVertex node1 = createNode(db, 1);
        OVertex node2 = createNode(db, 2);

        OVertex node0Class2 = db.newVertex("DecisionNode");
        node0Class2.setProperty("index", 0);
        node0Class2.save();

        OEdge edge1 = node0.addEdge(node0Class2, "Edge");
        edge1.save();
        OEdge edge1Class2 = node0Class2.addEdge(node1, "Edge");
        edge1Class2.save();
        OEdge edge2Class2 = node0Class2.addEdge(node2, "Edge");
        edge2Class2.save();
        OEdge edge2 = node1.addEdge(node1, "Edge");
        edge2.save();

        executeAQuery(db);


        db.close();
        orient.close();
    }

    private static OVertex createNode(ODatabaseSession db, Integer index) {
        OVertex result = db.newVertex("Node");
        result.setProperty("index", index);
        result.save();
        return result;
    }

    private static void executeAQuery(ODatabaseSession db) {
        String query = "SELECT expand(out('Edge').out('Edge')) from Node where index = ?";
        OResultSet rs = db.query(query, 0);
        rs.stream().forEach(x -> System.out.println("node: " + x.getProperty("index")));
        rs.close();
    }

}
