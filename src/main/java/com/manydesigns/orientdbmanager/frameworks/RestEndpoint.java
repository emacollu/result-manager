package com.manydesigns.orientdbmanager.frameworks;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Emanuele Collura
 * Date: 19/04/22
 * Time: 16:51
 */
@Getter @Setter
public class RestEndpoint {

    private RestMethod method;
    private String path;
    private String rootPath;
    private List<Parameter> parameters = new ArrayList<>();

    public enum RestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    public RestEndpoint(RestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        return "RestEndpoint{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", rootPath='" + rootPath + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
