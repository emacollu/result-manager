package com.manydesigns.orientdbmanager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manydesigns.orientdbmanager.models.Result;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Author: Emanuele Collura
 * Date: 11/04/22
 * Time: 16:51
 */
public class Utils {

    public static Result readResult(URL jsonResult) throws IOException, URISyntaxException {
        //Read from file
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(jsonResult.toURI()), Result.class);
    }
}
