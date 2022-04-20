package com.manydesigns.orientdbmanager.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Emanuele Collura
 * Date: 09/04/22
 * Time: 12:06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphElement implements Serializable {

    @JsonProperty("columns")
    private List<Column> columns;

    @JsonProperty("tuples")
    private List<List<TupleEl>> tuples;

}
