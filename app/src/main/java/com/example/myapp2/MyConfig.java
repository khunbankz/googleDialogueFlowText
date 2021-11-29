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
    //private String projectIdName;
//    private  final static String GITHUB_BASE_URL = "https://github.com/Sumethchan/credentials/blob/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json";
//    private final static String PARAM_QUERY = "q";
//    private final static String PARAM_SORT = "sort";
//    private final static String SORT_BY ="stars";
//    private  static URL buildUrl(String gitHubSearchQuery){
//        Uri buildUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
//                .appendQueryParameter(PARAM_QUERY,gitHubSearchQuery)
//                .appendQueryParameter(PARAM_SORT,SORT_BY)
//                .build();
//        URL url = null;
//        try {
//            url = new URL(buildUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return url;
//    }



    //    GoogleCredentials credentials;
//
//    {
//        try {
//            credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\main\\res\\raw\\application_default_credentials.json"));
//            projectId = ((ServiceAccountCredentials)credentials).getProjectId();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

        //return sessionsSettings;
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
        //getNames getId = new getNames();

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
            //new getNames().execute();
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
                        //countDownLatch.countDown();
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
        //new getNames().execute();

        //getNames getId = new getNames();
        //projectIdName = getId.doInBackground();
        //projectIdName = getId.credentialAsync;
        //this.projectId = getId.projectId;
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        //this.sessionsSettings = getId.sessionsSettings;
        //DialogueInputText dialogueInputText = new Dialogflowfactory().create();
        //dialogueInputText.init(new MyConfig(),new MyLog(),sessionsSettings);

//        String data = "";
//        try{
//            URL url = new URL("https://github.com/Sumethchan/credentials/blob/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            InputStream inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = bufferedReader.readLine()) != null){
//                data = data +line;
//
//            }
//            if(!data.isEmpty()){
//                JSONObject jsonObject = new JSONObject(data);
//                JSONObject users = jsonObject;
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        JSONParser parser = new JSONParser();
        //return null;
        //AssetManager assetFiles = context.getAssets();
//        try {
//            InputStream inputStream = assetFiles.open("application_default_credentials.json");
//            //InputStream stream = getResources().openRawResource(R.raw.application_default_credentials);
//            int sizeOffile = inputStream.available();
//            byte[] bufferData = new byte[sizeOffile];
//            inputStream.read(bufferData);
//            inputStream.close();
//            json = new String(bufferData,"UTF-8");
//            //JSONObject jsonObject = new JSONObject();
//            Object obj = parser.parse(json);
//            JSONObject jsonObject = (JSONObject) obj;
//            String project = (String) jsonObject.get(projectId);
//            return project;
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//            return " error";
//        }
        //return project;
//        try{
//            File file =new File(context.getFilesDir(),"application_default_credentials.json");
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line = bufferedReader.readLine();
//            while (line != null){
//                stringBuilder.append(line).append("\n");
//                line = bufferedReader.readLine();
//            }
//            bufferedReader.close();
//            // This responce will have Json Format String
//            String responce = stringBuilder.toString();

//            InputStream is = context.getAssets().open("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\application_default_credentials.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            String jsonString = new String(buffer, "UTF-8");

            //FileReader reader = new FileReader("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\application_default_credentials.json");
            //File jsonfile = new File("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\application_default_credentials.json");
            //Object obj = parser.parse(reader);
            //JSONObject jsonObject = (JSONObject) obj;
            //String project = (String) jsonObject.get(projectId);
            //this.projectIdName=project;
            //this.projectIdName="hello";
//            return responce;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "file not found";
//        }catch (IOException e) {
//            e.printStackTrace();
//            return "error IO";
//        }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return "error Parse";
//        }
        //System.out.println("projectidtest"+projectIdName);
        //return projectIdName;
    }

    @Override
    public void update(String name, String value) {
//        JSONParser parser = new JSONParser();
//        try {
//            Object obj = parser.parse(new FileReader("C:\\Program Files\\application_default_credentials.json"));
//            JSONObject jsonObject = (JSONObject) obj;
//            String project = (String) jsonObject.get(name);
//            this.projectIdName=project;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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
//        getNames getNa = new getNames();
//        this.sessionsSettings = getNa.sessionsSettings;

        //return;
        //return sessionsSettings;

    }
//    private class getNames extends AsyncTask<Void, Void, Void> {
//        private SessionsSettings sessionsSettings;
//        private String credentialAsync;
//
//        //private String googleCredential;
//
//        @Override
//        public Void doInBackground(Void... voids) {
////            Handler handler = new Handler();
////            String jsonString = handler.httpServiceCall(url);
//            String result = null;
//            //String urlJson = "https://raw.githubusercontent.com/Sumethchan/credentials/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json";
//            String urlJson = "https://drive.google.com/uc?id=11lAFE5kolY2J25NYQFcPafQ2o4ysxExh";
//            try {
//                System.out.println("requestUrl: "+urlJson);
//                URL url = new URL(urlJson);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                //InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
//                GoogleCredentials credentials =GoogleCredentials.fromStream(url.openStream());
//                SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
//                sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
//                System.out.println("Credential1: "+credentials);
//
//                String stringBuffer;
//                String string = "";
//                while ((stringBuffer = bufferedReader.readLine()) != null){
//                    string = String.format("%s%s", string, stringBuffer);
//                }
//                bufferedReader.close();
//                result = string;
//                //googleCredential = result;
//                //this.credentialAsync = result;
//
//                //result = convertResultToString(inputStream);
//                System.out.println("result"+result);
//
//
////                try {
////                    ObjectMapper objectMapper = new ObjectMapper();
////                    JsonNode jsonNode = objectMapper.readTree(result);
////                    String projectIdName= jsonNode.get("project_id").asText();
////                    //this.projectId = projectIdName;
////                    System.out.println("Real project id: "+projectIdName);
////                    System.out.println("jsonNode:"+jsonNode);
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }catch (ProtocolException e){
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//                        OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url("https://drive.google.com/uc?id=11lAFE5kolY2J25NYQFcPafQ2o4ysxExh").build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    if (response.isSuccessful()){
//                        String myResponse = response.body().string();
//                        InputStream is = response.body().byteStream();
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
//                        //GoogleCredentials credentials =GoogleCredentials.fromStream(url.openStream());
//                        //SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
//                        //sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
//                        ///System.out.println("Credential1: "+credentials);
//
////                        String stringBuffer;
////                        String string = "";
////                        while ((stringBuffer = bufferedReader.readLine()) != null){
////                            string = String.format("%s%s", string, stringBuffer);
////                        }
////                        bufferedReader.close();
//                        googleCredential = myResponse;
//                        System.out.println("Test OK http:"+myResponse);
//                    }
//                }
//            });
//
//            //JSONObject jsonObject = new JSONObject(jsonString)
//            //System.out.println("jsonString: "+result);
//
////            if(jsonString != null){
////                try {
////                    JSONObject jsonObject = new JSONObject(jsonString);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
//
//            return null;
//        }
//    }
}
