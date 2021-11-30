package com.example.myapp2;

import android.content.Context;
import android.os.AsyncTask;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.log.Log;

public class MyConfig implements Config {
    SessionsSettings sessionsSettings;
    String projectId;
    public String googleCredential;
    private Config config;
    private Log log;

    @Override
    public Integer getAsInteger(String name) {
        if (name == "maximum"){
            int maximuninput = 256;
            return maximuninput;
        }else if(name=="timeout"){
            int timeputsession = 3;
            return timeputsession;
        }
        else {
            return null;
        }
    }

    @Override
    public void update(String name, Integer value) {

    }

    @Override
    public Double getAsDouble(String name) {
        return null;
    }

    @Override
    public void update(String name, Double value) {

    }

    @Override
    public Float getAsFloat(String name) {
        return null;
    }

    @Override
    public void update(String name, Float value) {

    }

    @Override
    public Long getAsLong(String name) {
        return null;
    }

    @Override
    public void update(String name, Long value) {

    }

    @Override
    public String get(String value) {
        String projectIdName;
        Context context ;
        String json;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        if (value == "language"){
            return "th";
        }else if(value=="sesssionID"){
            return UUID.randomUUID().toString();
        }else if(value == "credential"){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://drive.google.com/uc?id=11lAFE5kolY2J25NYQFcPafQ2o4ysxExh").build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    countDownLatch.countDown();
                    e.printStackTrace();
                    log.error("credential","check connection");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()){
                        String myResponse1 = response.body().string();
                        countDownLatch.countDown();
                        System.out.println("Test OK http on get:"+myResponse1);
                        googleCredential= myResponse1;
                    }
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Myconfig projectId: "+googleCredential);
            return googleCredential;

        }
        return null;
    }

    @Override
    public void update(String name, String value) {
    }

    @Override
    public Boolean getAsBoolean(String name) {
        return null;
    }

    @Override
    public void update(String name, Boolean value) {

    }

    @Override
    public List<String> getAsList(String name) {
        return null;
    }

    @Override
    public void update(String name, List<String> value) {

    }

    @Override
    public String dump() {

        return null;
    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {

    }

}
