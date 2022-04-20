package com.manydesigns.orientdbmanager.result;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Emanuele Collura
 * Date: 16/04/22
 * Time: 11:05
 */
@Getter @Setter
public class ConditionalNode extends Node {

    private String condition;

    public ConditionalNode(String file, Integer line, String condition) {
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
