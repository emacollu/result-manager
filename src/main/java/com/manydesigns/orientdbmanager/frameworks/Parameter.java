package com.manydesigns.orientdbmanager.frameworks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Emanuele Collura
 * Date: 19/04/22
 * Time: 16:53
 */
@Getter
@Setter
@AllArgsConstructor
public class Parameter {

    private TypeParameter typeParameter;
    private String name;
    private String format;

    public static final String DEFAULT_FORMAT = "STRING";

    public enum TypeParameter {
        QUERY_PARAM,
        PATH_PARAM,
        JSON_BODY;

        public static TypeParameter getTypeFromMimeType(String mimeType) {
            if (mimeType.equals("application/json"))
                return JSON_BODY;
            if (mimeType.equals("query"))
                return QUERY_PARAM;
            if (mimeType.equals("path"))
                return PATH_PARAM;
            return null;
        }
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
