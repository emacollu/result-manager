package com.manydesigns.orientdbmanager.frameworks.implementation;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.manydesigns.orientdbmanager.frameworks.Framework;
import com.manydesigns.orientdbmanager.frameworks.Parameter;
import com.manydesigns.orientdbmanager.frameworks.RestEndpoint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * Author: Emanuele Collura
 * Date: 19/04/22
 * Time: 16:57
 */
@AllArgsConstructor
public class SpringBoot implements Framework {

    private Boolean findParameters;

    @Override
    public String pathController(ClassOrInterfaceDeclaration classDeclaration) {
        String path = "";

        for (var ann :
                classDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("RESTCONTROLLER")) {
                if (ann instanceof SingleMemberAnnotationExpr) {
                    path = String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", "");
                    break;
                }
            } else if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("REQUESTMAPPING")) {
                if (ann instanceof NormalAnnotationExpr) {
                    for (var p :
                            ((NormalAnnotationExpr) ann).getPairs()) {
                        if (p.getName().asString().toUpperCase(Locale.ROOT).equals("VALUE")) {
                            path = String.valueOf(p.getValue()).replace("\"", "");
                            break;
                        }

                    }
                    break;
                }
            }
        }

        return path;
    }

    @Override
    public RestEndpoint restMethod(MethodDeclaration methodDeclaration) {
        RestEndpoint restEndpoint = null;

        for (var ann :
                methodDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("GETMAPPING")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.GET, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("POSTMAPPING")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.POST, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PUTMAPPING")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.PUT, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("DELETEMAPPING")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.DELETE, "");
            }

            if (restEndpoint != null) {
                if (ann instanceof SingleMemberAnnotationExpr) {
                    restEndpoint.setPath(String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", ""));
                }

                break;
            }
        }

        if (restEndpoint != null && findParameters) {
            for (var param :
                    methodDeclaration.getParameters()) {
                for (var ann :
                        param.getAnnotations()) {
                    String nameAnnotation = ann.getName().getIdentifier().toUpperCase(Locale.ROOT);
                    if (nameAnnotation.equals("REQUESTPARAM")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.QUERY_PARAM,
                                        String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()).replace("\"", ""),
                                        Parameter.DEFAULT_FORMAT
                                )
                        );
                    }
                    if (nameAnnotation.equals("REQUESTBODY")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.JSON_BODY,
                                        null,
                                        param.getType().asString()
                                )
                        );
                    }
                    if (nameAnnotation.equals("PATHVARIABLE")) {
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
