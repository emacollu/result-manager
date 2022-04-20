package com.manydesigns.orientdbmanager.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Emanuele Collura
 * Date: 09/04/22
 * Time: 12:02
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {

    @JsonProperty("edges")
    private GraphElement edges;

    @JsonProperty("nodes")
    private GraphElement nodes;

    @JsonProperty("subpaths")
    private GraphElement subpaths;

    @JsonProperty("#select")
    private GraphElement select;

}
