package com.manydesigns.orientdbmanager;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Author: Emanuele Collura
 * Date: 11/04/22
 * Time: 20:01
 */
public class JavaParserTest {

    public static void main(String[] args) throws FileNotFoundException {

        CompilationUnit compilationUnit = StaticJavaParser.parse(new File("/Users/emanuelecollura/progetti/javacommandinjection/src/main/java/com/manydesigns/javacommandinjection/CommandController.java"));

        assert compilationUnit != null;

        var methods = compilationUnit.findAll(MethodDeclaration.class);
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).get(0).getAnnotations();

        compilationUnit.findAll(IfStmt.class);
        compilationUnit.findAll(WhileStmt.class);
        compilationUnit.findAll(DoStmt.class);
        compilationUnit.findAll(ForEachStmt.class);
        compilationUnit.findAll(ForStmt.class);
        compilationUnit.findAll(SwitchStmt.class);

    }


    /*
    String httpMethod = null;
String path = "";
for(var ann : methods.get(0).getAnnotations()) {
    if(ann.getName().getIdentifier().toUpperCase(Locale.ROOT).contains("GET")) {
        httpMethod = "GET";
        break;
    }
}
methods.get(1).getAnnotations().get(0).getChildNodes()

     */

}
