package com.manydesigns.orientdbmanager.frameworks.implementation;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.manydesigns.orientdbmanager.frameworks.Framework;
import com.manydesigns.orientdbmanager.frameworks.Parameter;
import com.manydesigns.orientdbmanager.frameworks.RestEndpoint;
import lombok.AllArgsConstructor;

import java.util.Locale;

/**
 * Author: Emanuele Collura
 * Date: 20/04/22
 * Time: 17:38
 */
@AllArgsConstructor
public class JaxRS implements Framework {

    private Boolean findParameters;

    @Override
    public String pathController(ClassOrInterfaceDeclaration classDeclaration) {
        String path = "";

        for (var ann :
                classDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PATH")) {
                if (ann instanceof SingleMemberAnnotationExpr) {
                    path = String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", "");
                }
                break;
            }
        }

        return path;
    }

    @Override
    public RestEndpoint restMethod(MethodDeclaration methodDeclaration) {
        RestEndpoint restEndpoint = null;
        String path = "";

        for (var ann :
                methodDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("GET")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.GET, path);
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("POST")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.POST, path);
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PUT")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.PUT, path);
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("DELETE")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.DELETE, path);
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PATH") && ann instanceof SingleMemberAnnotationExpr) {
                path = String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", "");
            }
        }

        if (restEndpoint != null && findParameters) {
            restEndpoint.setPath(path);
            for (var param :
                    methodDeclaration.getParameters()) {
                for (var ann :
                        param.getAnnotations()) {
                    String nameAnnotation = ann.getName().getIdentifier().toUpperCase(Locale.ROOT);
                    if (nameAnnotation.equals("QUERYPARAM")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.QUERY_PARAM,
                                        String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", ""),
                                        Parameter.DEFAULT_FORMAT
                                )
                        );
                    }
                    if (nameAnnotation.equals("PATHPARAM")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.PATH_PARAM,
                                        String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", ""),
                                        Parameter.DEFAULT_FORMAT
                                )
                        );
                    }
                }
            }
        }

        return restEndpoint;
    }

}
