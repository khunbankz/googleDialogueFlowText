
package com.example.googledialogflow.json;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Example {

    @SerializedName("query_input")
    @Expose
    private QueryInput queryInput;

    public QueryInput getQueryInput() {
        return queryInput;
    }

    public void setQueryInput(QueryInput queryInput) {
        this.queryInput = queryInput;
    }

}
