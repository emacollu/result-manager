package com.manydesigns.orientdbmanager.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.manydesigns.orientdbmanager.Constants.COMMON_PATH;

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
                "file='" + file.replace(COMMON_PATH, "") + '\'' +
                ", line=" + line +
                '}';
    }
}
