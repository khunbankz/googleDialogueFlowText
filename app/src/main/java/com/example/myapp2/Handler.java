package com.example.myapp2;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Handler {
    private static final String TAG =Handler.class.getSimpleName();
    public Handler(){

    }
    public String httpServiceCall(String requestUrl){
        String result = null;
        try {
            System.out.println("requestUrl: "+requestUrl);
            URL url = new URL(requestUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            //InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(bufferedReader);
//            JSONObject jsonObject = (JSONObject) obj;
//            String project = (String) jsonObject.get("project_id");
//            System.out.println("projectIDname"+project);
            String stringBuffer;
            String string = "";
            while ((stringBuffer = bufferedReader.readLine()) != null){
                string = String.format("%s%s", string, stringBuffer);
            }
            bufferedReader.close();
            result = string;
            //result = convertResultToString(inputStream);
            System.out.println("result"+result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (ProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

//    private String convertResultToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//        StringBuilder stringBuilder = new StringBuilder();
//        String li;
//        while(true){
//            try {
//                if (!((li = bufferedReader.readLine()) != null)){
//                    stringBuilder.append('\n');
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("strBuilder"+stringBuilder.toString());
//            return stringBuilder.toString();
//        }
//    }
}
