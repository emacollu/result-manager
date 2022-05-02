package com.manydesigns.orientdbmanager;

import com.orientechnologies.orient.core.metadata.schema.OType;

import java.net.URL;

/**
 * Author: Emanuele Collura
 * Date: 12/04/22
 * Time: 20:32
 */
public class Constants {

    public static final String NAME_DB = "com_inj_db";
    public static final String USER_DB = "root";
    public static final String PW_DB = "rootpwd";

    public static final URL JSON_RESULT = SetupDB.class.getClassLoader().getResource("results_analysis_com_inj.json");
    public static final URL OPENAPI = SetupDB.class.getClassLoader().getResource("openapi.yaml");
    public static final String NODE_KEY = "Node";
    public static final String CONDITIONAL_NODE_KEY = "ConditionalNode";
    public static final String EDGE_KEY = "Edge";
    public static final String COMMON_PATH = "file:/Users/emanuelecollura/progetti/javacommandinjection/codeql_source/src/main/java/com/manydesigns/javacommandinjection/";

    public interface VProperty {
        default String getName() {
            return "property";
        }

        default OType getType() {
            return OType.INTEGER;
        }

        default boolean isUnique() {
            return false;
        }
    }

    public enum NodeVProperty implements VProperty {
        ID("id", OType.INTEGER, true),
        LABEL("label", OType.STRING),
        FILE("file", OType.STRING),
        START_LINE("startLine", OType.INTEGER),
        END_LINE("endLine", OType.INTEGER),
        START_COLUMN("startColumn", OType.INTEGER),
        END_COLUMN("endColumn", OType.INTEGER);

        private final String name;
        private final OType type;
        private final boolean unique;

        NodeVProperty(String name, OType type) {
            this(name, type, false);
        }

        NodeVProperty(String name, OType type, boolean unique) {
            this.name = name;
            this.type = type;
            this.unique = unique;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public OType getType() {
            return type;
        }

        @Override
        public boolean isUnique() {
            return unique;
        }
    }

    public enum ConditionalNodeVProperty implements VProperty {
        ID("id", OType.STRING, true),
        CONDITION("condition", OType.STRING),
        LINE("startLine", OType.INTEGER);

        private final String name;
        private final OType type;
        private final boolean unique;

        ConditionalNodeVProperty(String name, OType type) {
            this(name, type, false);
        }

        ConditionalNodeVProperty(String name, OType type, boolean unique) {
            this.name = name;
            this.type = type;
            this.unique = unique;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public OType getType() {
            return type;
        }

        @Override
        public boolean isUnique() {
            return unique;
        }
    }

}
