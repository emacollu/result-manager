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
 * Time: 12:12
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Url implements Serializable {

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("startLine")
    private Integer startLine;

    @JsonProperty("startColumn")
    private Integer startColumn;

    @JsonProperty("endLine")
    private Integer endLine;

    @JsonProperty("endColumn")
    private Integer endColumn;

}
