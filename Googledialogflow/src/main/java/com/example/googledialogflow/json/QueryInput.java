
package com.example.googledialogflow.json;

import javax.annotation.Generated;

import com.example.googledialogflow.json.Text;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class QueryInput {

    @SerializedName("text")
    @Expose
    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
