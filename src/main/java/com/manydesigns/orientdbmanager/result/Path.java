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
@Getter @Setter
public class Path {

    private Node start;
    private Node end;
    private LinkedList<Node> steps = new LinkedList<>();
    private List<ConditionalNode> conditions = new ArrayList<>();
    private RestEndpoint restEndpoint;

    public Path(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public void addStep(Node node) {
        steps.addLast(node);
    }

    public void addConditionalNode(ConditionalNode conditionalNode) {
        for (var condition : conditions) {
            if(Objects.equals(condition.getFile(), conditionalNode.getFile()) &&
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
                ", conditions=" + conditions +
                ", restEndpoint=" + restEndpoint +
                '}';
    }
}
