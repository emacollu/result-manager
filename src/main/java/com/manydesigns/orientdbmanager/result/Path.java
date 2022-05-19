package com.manydesigns.orientdbmanager.result;

import com.manydesigns.orientdbmanager.frameworks.RestEndpoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Emanuele Collura
 * Date: 15/04/22
 * Time: 17:31
 */
@Getter
@Setter
public class Path {

    private Node start;
    private Node end;
    private String nameCmdVar;
    private LinkedList<Node> steps = new LinkedList<>();
    private List<Node> subSteps = new ArrayList<>();
    private List<ConditionalNode> conditions = new ArrayList<>();
    private RestEndpoint restEndpoint;

    public Path(Node start, Node end, String nameCmdVar) {
        this.start = start;
        this.end = end;
        this.nameCmdVar = nameCmdVar;
    }

    public void addStep(Node node) {
        steps.addLast(node);
    }

    public void addSubStep(Node node) {
        for (var subStep : subSteps) {
            if (Objects.equals(subStep.getFile(), node.getFile()) &&
                    Objects.equals(subStep.getLine(), node.getLine())) {
                return;
            }
        }
        subSteps.add(node);
    }

    public void addConditionalNode(ConditionalNode conditionalNode) {
        for (var condition : conditions) {
            if (Objects.equals(condition.getFile(), conditionalNode.getFile()) &&
                    Objects.equals(condition.getLine(), conditionalNode.getLine())) {
                return;
            }
        }
        conditions.add(conditionalNode);
    }

    @Override
    public String toString() {
        return "Path{" +
                "start=" + start +
                ", end=" + end +
                ", steps=" + steps +
                ", subSteps=" + subSteps +
                ", conditions=" + conditions +
                ", restEndpoint=" + restEndpoint +
                '}';
    }
}
