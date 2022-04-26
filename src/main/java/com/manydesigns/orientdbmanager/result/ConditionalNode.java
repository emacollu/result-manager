package com.manydesigns.orientdbmanager.result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Emanuele Collura
 * Date: 16/04/22
 * Time: 11:05
 */
@Getter
@Setter
public class ConditionalNode extends Node {

    @JsonSerialize(using = CustomSerializeExpression.class)
    private ConditionalExpression condition;

    public ConditionalNode(String file, Integer line, ConditionalExpression condition) {
        super(file, line);
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ConditionalNode{" +
                "condition='" + condition + '\'' +
                ", file='" + file + '\'' +
                ", line=" + line +
                '}';
    }
}
