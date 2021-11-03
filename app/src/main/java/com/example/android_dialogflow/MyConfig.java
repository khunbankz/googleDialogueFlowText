package com.example.android_dialogflow;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.log.Log;

public class MyConfig implements Config {
    final String credUrl =
            "https://raw.githubusercontent.com/Weerapat1455/And_cred/main/credential.json";
    private String result;
//    private boolean check = false;

    @Override
    public Integer getAsInteger(String name) {
        if (name.equals("maxInputLength")) {
            int maxInputLength = 7;
            return maxInputLength;
        }
        if (name.equals("maxResponseTime")) {
            int maxInputLength = 5;
            return maxInputLength;
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
//        if (name.equals("setCredentials")) {
//            new GetCredUrl().execute();
//            System.out.println("result 1 " + result);
//            return "credentials checked";
//        }
        if (name.equals("getCredentials")) {
            new GetCredUrl().execute();
            try {
                Thread.sleep(1000);
                System.out.println("result 2 " + result);
                return result;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

//    public void getSession(SessionsSettings sessionsSettings) {
//        this.sessionsSettings = sessionsSettings;
//        System.out.println("MyConfig : " + sessionsSettings);
//    }

    private class GetCredUrl extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            try {
                System.out.println("result test " + result);
                url = new URL(credUrl);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    string = String.format("%s%s", string, stringBuffer);
//                    check = true;
                }
                bufferedReader.close();
                result = string;
                System.out.println("result run " + result);

//                InputStream test = new InputStreamReader(url.openStream());
//                credentials = GoogleCredentials.fromStream(test);
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
                System.out.println("result ex " + result);
            }
            return null;
        }
    }

}
