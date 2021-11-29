package com.example.googledialogflow;

import com.google.gson.annotations.SerializedName;

public class Post {
//    private int userId;
//    private  int id;
//    private String title;
    @SerializedName("body")
    private String text;
    private String language;

//    public int getUserId() {
//        return userId;
//    }

//    public int getId() {
//        return id;
//    }

//    public String getTitle() {
//        return title;
//    }

//    public String getText() {
//        return text;
//    }
//    public String getLanguage() {
//        return language;
//    }
    Post(String text,String language){
        this.text = text;
        this.language = language;
    }
}
