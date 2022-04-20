package com.manydesigns.orientdbmanager.frameworks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Emanuele Collura
 * Date: 19/04/22
 * Time: 16:53
 */
@Getter @Setter @AllArgsConstructor
public class Parameter {

    private TypeParameter typeParameter;
    private String name;
    private String format;

    public static final String DEFAULT_FORMAT = "STRING";

    enum TypeParameter {
        QUERY_PARAM,
        PATH_PARAM,
        JSON_BODY
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "typeParameter=" + typeParameter +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
