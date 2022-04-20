package com.manydesigns.orientdbmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Emanuele Collura
 * Date: 09/04/22
 * Time: 12:17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TupleEl implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("url")
    private Url url;

    public TupleEl(String label) {
        this.label = label;
    }
}
