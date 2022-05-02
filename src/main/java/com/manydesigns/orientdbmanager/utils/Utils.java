package com.manydesigns.orientdbmanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.manydesigns.orientdbmanager.models.Result;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Author: Emanuele Collura
 * Date: 11/04/22
 * Time: 16:51
 */
public class Utils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    public static Result readResult(URL jsonResult) throws IOException, URISyntaxException {
        //Read from file
        return JSON_MAPPER.readValue(new File(jsonResult.toURI()), Result.class);
    }

    public static Map readOpenAPI(URL openAPI) throws IOException, URISyntaxException {
        //Read from file
        return YAML_MAPPER.readValue(new File(openAPI.toURI()), Map.class);
    }

    public static String toJSON(Object object) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(object);
    }

}
