package com.example.android_dialogflow;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.log.Log;

public class MyConfig implements Config {
    final String credUrl =
//            "https://raw.githubusercontent.com/Weerapat1455/And_cred/main/credential.json";
            "https://drive.google.com/uc?id=10edZa3D8Sw-AZYP6UMfekDHMEj_q2aEz";
    private int maxResponseTime;
    private String cred;

    @Override
    public Integer getAsInteger(String name) {
        if (name.equals("maxInputLength")) {
            int maxInputLength = 7;
            return maxInputLength;
        }
        if (name.equals("maxResponseTime")) {
            maxResponseTime = 2;
            return maxResponseTime;
        }
        return null;
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
    public String get(String name) {
        if (name.equals("getCredentials")) {
//            final int[] statusCode = new int[1];
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(maxResponseTime, TimeUnit.SECONDS)   // overall call
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(credUrl)
                    .build();

            Call call = client.newCall(request);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    statusCode[0] = response.code();
                    System.out.println("response code "+response.code());
                    if (!response.isSuccessful()) {
                        System.out.println("error : response code = " + response.code());
                        throw new IOException("Unexpected code " + response);
                    } else {
                        System.out.println(response.receivedResponseAtMillis()-response.sentRequestAtMillis());    //404:false
//                        System.out.println("isCanceled "+call.isCanceled());
                        cred = response.body().string();
                         System.out.println("cred\n" + cred);
//                        System.out.println("isCanceled_o "+call.isCanceled());
                    }
//                    System.out.println("isCanceled_o "+call.isCanceled());
                    countDownLatch.countDown();
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("isCanceled(failure) "+call.isCanceled());
                    System.out.println("isExecuted(failure) "+call.isExecuted());
                    e.printStackTrace();
//                    System.out.println("Timeout");
                    countDownLatch.countDown();

                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(cred);
            return cred;
        }

        if (name.equals("languageCode")) {
            String language;

            language = "en";
//            language = "th";
            return language;
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
        return "dump check";
    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {

    }
}
