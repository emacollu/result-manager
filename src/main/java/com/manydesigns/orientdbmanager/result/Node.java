package com.manydesigns.orientdbmanager.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Author: Emanuele Collura
 * Date: 15/04/22
 * Time: 17:32
 */
@Getter
@Setter
@AllArgsConstructor
public class Node {

    protected String file;
    protected Integer line;

    @Override
    public String toString() {
        return "Node{" +
                "file='" + file + '\'' +
                ", line=" + line +
                '}';
    }
}
