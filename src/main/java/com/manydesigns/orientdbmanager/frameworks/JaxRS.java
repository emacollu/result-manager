package com.manydesigns.orientdbmanager.frameworks;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.util.Locale;

/**
 * Author: Emanuele Collura
 * Date: 20/04/22
 * Time: 17:38
 */
public class JaxRS implements Framework{

    @Override
    public String pathController(ClassOrInterfaceDeclaration classDeclaration) {
        String path = "";

        for (var ann :
                classDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PATH")) {
                if (ann instanceof SingleMemberAnnotationExpr) {
                    path = String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue());
                }
                break;
            }
        }

        return path;
    }

    @Override
    public RestEndpoint restMethod(MethodDeclaration methodDeclaration) {
        RestEndpoint restEndpoint = null;

        for (var ann :
                methodDeclaration.getAnnotations()) {
            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("GET")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.GET, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("POST")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.POST, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("PUT")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.PUT, "");
            }

            if (ann.getName().getIdentifier().toUpperCase(Locale.ROOT).equals("DELETE")) {
                restEndpoint = new RestEndpoint(RestEndpoint.RestMethod.DELETE, "");
            }

            if(restEndpoint != null) {
                if (ann instanceof SingleMemberAnnotationExpr) {
                    restEndpoint.setPath(String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()));
                }

                break;
            }
        }

        if (restEndpoint != null) {
            for (var param :
                    methodDeclaration.getParameters()) {
                for (var ann :
                        param.getAnnotations()) {
                    String nameAnnotation = ann.getName().getIdentifier().toUpperCase(Locale.ROOT);
                    if (nameAnnotation.equals("QUERYPARAM")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.QUERY_PARAM,
                                        String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()),
                                        Parameter.DEFAULT_FORMAT
                                )
                        );
                    }
                    if (nameAnnotation.equals("PATHPARAM")) {
                        restEndpoint.addParameter(
                                new Parameter(
                                        Parameter.TypeParameter.PATH_PARAM,
                                        String.valueOf(((SingleMemberAnnotationExpr) ann).getMemberValue()),
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
