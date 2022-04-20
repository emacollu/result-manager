package com.manydesigns.orientdbmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.manydesigns.orientdbmanager.frameworks.Parameter;
import com.manydesigns.orientdbmanager.frameworks.SpringBoot;
import com.manydesigns.orientdbmanager.models.TupleEl;
import com.manydesigns.orientdbmanager.result.ConditionalNode;
import com.manydesigns.orientdbmanager.result.Node;
import com.manydesigns.orientdbmanager.result.Path;
import com.manydesigns.orientdbmanager.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.manydesigns.orientdbmanager.Constants.JSON_RESULT;

/**
 * Author: Emanuele Collura
 * Date: 15/04/22
 * Time: 17:21
 */
@Slf4j
public class Management {

    private static final int INDEX_END_SELECT = 0;
    private static final int INDEX_START_SELECT = 1;
    private static final Map<String, CompilationUnit> parsedFiles = new HashMap<>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        assert JSON_RESULT != null;
        var result = Utils.readResult(JSON_RESULT);

        List<Path> paths = new ArrayList<>();

        for (var tuple :
                result.getSelect().getTuples()) {
            TupleEl startEl = tuple.get(INDEX_START_SELECT);
            TupleEl endEl = tuple.get(INDEX_END_SELECT);

            var path = new Path(
                    new Node(startEl.getUrl().getUri(), startEl.getUrl().getStartLine()),
                    new Node(endEl.getUrl().getUri(), endEl.getUrl().getStartLine())
            );

            path.addStep(
                    new Node(startEl.getUrl().getUri(), startEl.getUrl().getStartLine())
            );

            var currentNode = startEl.getId();
            var level = 1;
            var lastEdgeChecked = 0;
            do {
                var edgeTuples = result.getEdges().getTuples();
                while (lastEdgeChecked < edgeTuples.size()) {
                    var edge = edgeTuples.get(lastEdgeChecked);
                    if (edge.get(0).getId().equals(currentNode)) {
                        TupleEl newStep = edge.get(1);

                        var conditionalNodes = getConditions(newStep);
                        for (var condition : conditionalNodes) {
                            path.addConditionalNode(condition);

                        }

                        path.addStep(
                                new Node(newStep.getUrl().getUri(), newStep.getUrl().getStartLine())
                        );
                        currentNode = newStep.getId();
                        break;
                    }

                    lastEdgeChecked++;
                }

                level++;
            } while (!Objects.equals(currentNode, endEl.getId()) && level <= result.getNodes().getTuples().size());

            paths.add(path);
        }

        var springBoot = new SpringBoot();

        for (var path :
                paths) {
            var startNode = path.getStart();
            var compilationUnit = getParsedFile(startNode.getFile());

            MethodDeclaration methodDeclaration = null;
            for (var method :
                    compilationUnit.findAll(MethodDeclaration.class)) {
                if (isInStmt(method, startNode.getLine())) {
                    methodDeclaration = method;
                    break;
                }
            }
            assert methodDeclaration != null;
            var restEndpoint = springBoot.restMethod(methodDeclaration);

            ClassOrInterfaceDeclaration classDeclaration = null;
            for (var cd :
                    compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
                if (isInStmt(cd, startNode.getLine())) {
                    classDeclaration = cd;
                    break;
                }
            }
            assert classDeclaration != null;

            var rootPath = springBoot.pathController(classDeclaration);
            if (rootPath != null)
                restEndpoint.setRootPath(rootPath);

            for (var parameter : restEndpoint.getParameters()) {
                if (!Objects.equals(parameter.getFormat(), Parameter.DEFAULT_FORMAT)) {
                    var urlClass = getUrlFile(parameter.getFormat(), result.getNodes().getTuples());
                    if (urlClass != null) {
                        var formatClass = getParsedFile(urlClass);
                        ClassOrInterfaceDeclaration declaration = null;
                        for (var candidateClass :
                                formatClass.findAll(ClassOrInterfaceDeclaration.class)) {
                            if (candidateClass.getName().asString().equalsIgnoreCase(parameter.getFormat())) {
                                declaration = candidateClass;
                                break;
                            }
                        }
                        assert declaration != null;
                        List<String> format = new ArrayList<>();
                        for (var field :
                                declaration.getFields()) {
                            VariableDeclarator variable = field.getVariable(0);
                            format.add("\":{\"key\":" + variable.getNameAsString() + ", \"type\":" + variable.getTypeAsString() + "\"}");
                        }
                        parameter.setFormat("[" + String.join(",", format) + "]");
                    }
                }
            }

            path.setRestEndpoint(restEndpoint);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(paths));

    }

    private static String getUrlFile(String className, List<List<TupleEl>> nodes) {
        String urlClass = null;

        for (var url :
                parsedFiles.keySet()) {
            var urlSplit = url.split("/");
            if (urlSplit[urlSplit.length - 1].equalsIgnoreCase(className + ".java")) {
                urlClass = url;
                break;
            }
        }

        if (urlClass == null) {
            for (var tuple :
                    nodes) {
                for (var node :
                        tuple) {
                    if (node.getUrl() != null) {
                        var url = node.getUrl().getUri();
                        var urlSplit = url.split("/");
                        if (urlSplit[urlSplit.length - 1].equalsIgnoreCase(className + ".java")) {
                            urlClass = url;
                            break;
                        }
                    }
                }
                if (urlClass != null)
                    break;
            }
        }

        return urlClass;
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

    private static List<ConditionalNode> getConditions(TupleEl node) {
        List<ConditionalNode> result = new ArrayList<>();
        CompilationUnit compilationUnit = getParsedFile(node.getUrl().getUri());
        result.addAll(getIfConditions(compilationUnit, node));
        result.addAll(getWhileConditions(compilationUnit, node));
        result.addAll(getSwitchConditions(compilationUnit, node)); //TODO to check
        return result;
    }

    private static List<ConditionalNode> getIfConditions(CompilationUnit compilationUnit, TupleEl node) {
        List<ConditionalNode> result = new ArrayList<>();
        for (var ifStmt :
                compilationUnit.findAll(IfStmt.class)) {
            var condition = ifStmt.getCondition();
            var lineIf = getStartPosition(ifStmt).line;
            var lineNode = node.getUrl().getStartLine();

            if (lineIf > lineNode)
                break;

            if (notInCondition(condition, node)) {
                if (isInStmt(ifStmt.getThenStmt(), lineNode)) {
                    result.add(new ConditionalNode(
                            node.getUrl().getUri(),
                            lineIf,
                            condition.toString()
                    ));
                }

                Optional<Statement> elseStmt = ifStmt.getElseStmt();
                if (elseStmt.isPresent() && isInStmt(elseStmt.get(), lineNode)) {
                    result.add(new ConditionalNode(
                            node.getUrl().getUri(),
                            lineIf,
                            "!(" + condition.toString() + ")"
                    ));
                }
            }
        }
        return result;
    }

    private static List<ConditionalNode> getWhileConditions(CompilationUnit compilationUnit, TupleEl node) {
        List<ConditionalNode> result = new ArrayList<>();
        for (var whileStmt :
                compilationUnit.findAll(WhileStmt.class)) {
            var condition = whileStmt.getCondition();
            var lineWhile = getStartPosition(whileStmt).line;
            var lineNode = node.getUrl().getStartLine();

            if (lineWhile > lineNode)
                break;

            if (notInCondition(condition, node) && isInStmt(whileStmt.getBody(), lineNode)) {
                result.add(new ConditionalNode(
                        node.getUrl().getUri(),
                        lineWhile,
                        condition.toString()
                ));
            }
        }
        return result;
    }

    private static List<ConditionalNode> getSwitchConditions(CompilationUnit compilationUnit, TupleEl node) {
        List<ConditionalNode> result = new ArrayList<>();
        for (var switchStmt :
                compilationUnit.findAll(SwitchStmt.class)) {
            var condition = switchStmt.getSelector();
            var lineSwitch = getStartPosition(switchStmt).line;
            var lineNode = node.getUrl().getStartLine();

            if (lineSwitch > lineNode)
                break;

            if (notInCondition(condition, node)) {
                for (var entry :
                        switchStmt.getEntries()) {
                    for (var stmt :
                            entry.getStatements()) {
                        if (isInStmt(stmt, lineNode)) {
                            result.add(new ConditionalNode(
                                    node.getUrl().getUri(),
                                    lineSwitch,
                                    condition.toString() + " = " + entry.getLabels().toString()
                            ));
                        }
                    }

                }
            }
        }
        return result;
    }

    private static boolean isInStmt(com.github.javaparser.ast.Node statement, Integer lineNode) {
        int stmtBeginLine = getStartPosition(statement).line;
        int stmtEndLine = getEndPosition(statement).line;

        return lineNode > stmtBeginLine
                && lineNode < stmtEndLine;
    }

    private static boolean notInCondition(Expression condition, TupleEl node) {
        var startColumnNode = node.getUrl().getStartColumn();
        var startLine = node.getUrl().getStartLine();

        Position startPosition = getStartPosition(condition);
        Position endPosition = getEndPosition(condition);
        return (startLine < startPosition.line || startLine >= endPosition.line)
                || (startColumnNode < startPosition.column || startColumnNode >= endPosition.column);
    }

    private static Position getStartPosition(com.github.javaparser.ast.Node node) {
        Optional<Position> nodeBegin = node.getBegin();
        assert nodeBegin.isPresent();
        return nodeBegin.get();
    }

    private static Position getEndPosition(com.github.javaparser.ast.Node node) {
        Optional<Position> nodeEnd = node.getEnd();
        assert nodeEnd.isPresent();
        return nodeEnd.get();
    }

}
