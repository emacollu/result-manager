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
 * Time: 12:08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Column implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("kind")
    private String kind;

}
