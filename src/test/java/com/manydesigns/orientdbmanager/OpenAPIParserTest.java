package com.manydesigns.orientdbmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Author: Emanuele Collura
 * Date: 01/05/22
 * Time: 14:34
 */
public class OpenAPIParserTest {

    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        System.out.println(mapper.readValue(new File("/Users/emanuelecollura/progetti/orientdb-manager/src/main/resources/openapi.yaml"), Map.class));
    }

}
