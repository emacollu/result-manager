package com.manydesigns.orientdbmanager.frameworks;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Author: Emanuele Collura
 * Date: 19/04/22
 * Time: 16:44
 */
public interface Framework {

    String pathController(ClassOrInterfaceDeclaration classDeclaration);
    RestEndpoint restMethod(MethodDeclaration methodDeclaration);

}
