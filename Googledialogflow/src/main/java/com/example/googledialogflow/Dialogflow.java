package com.example.googledialogflow;



import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
//import com.google.common.collect.Maps;
//
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;
import th.co.ais.genesis.blueprint.log.Log;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
import java.util.concurrent.TimeUnit;

public class Dialogflow implements DialogueInputText{
    private  String jsonCredential;
    private  String projectId;
    //private  String projectIdhaha ="";
    private  String sessionId;
    private SessionsSettings sessionsSettings;
    private String languageCode;
    private String responsemessage;
    private ResultListener listener;
    private Config config;
    private Log log;
    private  int length;
    //String oldmessage ;
    private int maximum;
    private String messageInput;
    //String projectIdhaha = "";
    private String messageOld;
    private long startTimes;
    private long endTimes;
    private long duration;


//    public void configId(String projectID,String sessionID,SessionsSettings sessionsSettings){
////        this.projectId =projectID;
////        this.sessionId = sessionID;
//        //this.sessionsSettings =sessionsSettings;
//        System.out.println("projectID: "+projectID);
//        System.out.println("sessionID: "+sessionID);
//        //System.out.println("sessionsSettings: "+sessionsSettings);
//
//    }
//    public void speechs(String input){
//
//        List<String> texts = new ArrayList<>();
//        texts.add(input);
//        Map<String, QueryResult> queryResults = Maps.newHashMap();
//        try(SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)){
//            SessionName session = SessionName.of(projectId, sessionId);
//            System.out.println("Session Path: " + session.toString());
//
//            // Detect intents for each text input
//            for (String text : texts) {
//                // Set the text (hello) and language code (en-US) for the query
//                String message ;
//                TextInput.Builder textInput =
//                        TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
//
//                // Build the query with the TextInput
//                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
//
//                // Performs the detect intent request
//                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
//
//                // Display the query result
//                QueryResult queryResult = response.getQueryResult();
//
//                System.out.println("====================");
//                System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
//                System.out.format(
//                        "Detected Intent: %s (confidence: %f)\n",
//                        queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
//                System.out.format(
//                        "Fulfillment Text: '%s'\n",
//                        queryResult.getFulfillmentMessagesCount() > 0
//                                ? queryResult.getFulfillmentMessages(0).getText()
//                                : "Triggered Default Fallback Intent");
//                System.out.println("answer_response: "+queryResult.getFulfillmentText());
//                message = queryResult.getFulfillmentText();
//                this.responsemessage = message;
//
//                //System.out.println(messa);
//
//
//                queryResults.put(text, queryResult);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        this.listener.OnResult(responsemessage,1.0,"");
//    }

    @Override
    public void open(ResultListener listener) {

        this.listener = listener;
        this.listener.OnResult(responsemessage,1.0,"");
    }

    @Override
    public void speech(String input) {

        InputStream stream = new ByteArrayInputStream(jsonCredential.getBytes(StandardCharsets.UTF_8));
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(stream);
            //credentials.refreshIfExpired();

            //AccessToken token = credentials.getAccessToken();
            //System.out.println("Access Token"+ token);
            projectId = ((ServiceAccountCredentials)credentials).getProjectId();
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            settingsBuilder
                    .detectIntentSettings()
                    .setRetrySettings(
                            settingsBuilder
                                    .detectIntentSettings()
                                    .getRetrySettings()
                                    .toBuilder()
                                    .setTotalTimeout(Duration.ofSeconds(1))
                                    .build());
            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        GoogleCredentials credentials;
//        String projectIdhaha = "";
//        {
//            try {
//                credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\main\\res\\raw\\application_default_credentials.json"));
//                credentials.refreshIfExpired();
//                projectIdhaha = ((ServiceAccountCredentials)credentials).getProjectId();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        this.startTimes = System.currentTimeMillis();
        //config.init(config,log,sessionsSettings);
        length = input.length();
        if (length > maximum){
            log.error("Error:"," Too many characters input ");
            log.error("Error:","Your input have "+length+" characters");
            log.error("Error:","Maximun input is "+maximum+" characters");
        }
        List<String> texts = new ArrayList<>();
        texts.add(input);
        //config.get("hello");
        //Map<String, QueryResult> queryResults = Maps.newHashMap();
        try(SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)){
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());
            this.log.debug(projectId,sessionId);
            //Thread.sleep(1000);
            // Detect intents for each text input
            for (String text : texts) {
                // Set the text (hello) and language code (en-US) for the query
                String message ;

                TextInput.Builder textInput =
                        TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

                // Build the query with the TextInput
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
                System.out.println("QueryInput"+queryInput);

                // Performs the detect intent request
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

                // Display the query result
                QueryResult queryResult = response.getQueryResult();
                //QueryResult queryResult = null;
                messageInput = queryResult.getQueryText();
                if (messageInput == null){
                    log.error("Error:","Input message empty");
                }
                System.out.println("====================");
                System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
                System.out.format(
                        "Detected Intent: %s (confidence: %f)\n",
                        queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
                System.out.format(
                        "Fulfillment Text: '%s'\n",
                        queryResult.getFulfillmentMessagesCount() > 0
                                ? queryResult.getFulfillmentMessages(0).getText()
                                : "Triggered Default Fallback Intent");
                System.out.println("answer_response: "+queryResult.getFulfillmentText());
                //System.out.println("answer_response: "+queryResult);
                //config.get("project_id");e
                //System.out.println("print test:");
                //System.out.println("Jsonread" + jsonCredential);
                //System.out.println("sessionsSettings:"+sessionsSettings);
                //System.out.println("sessionsSettings1:");


                message = queryResult.getFulfillmentText();
                //message = null;
                //long endTimes = System.currentTimeMillis();
                //long duration = endTimes-startTimes;
                this.responsemessage = message;
                this.log.info(message, String.valueOf(queryResult.getIntentDetectionConfidence()));
//                if (message == oldmessage){
//                    log.error("Error","check your connection");
//                }
//                oldmessage = message;

                //this.listener = message;
                //this.responsemessage = message;
                //JSONParser parser = new JSONParser();
                //InputStream stream = getClass().getResource("application_default_credentials.json");
                //URL url = getClass().getResource("application_default_credentials.json");
                //FileReader reader = new FileReader("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\Googledialogflow\\src\\application_default_credentials.json");
                    //File jsonfile = new File("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\application_default_credentials.json");
                    //Object obj = parser.parse(reader);
                    //JSONObject jsonObject = (JSONObject) obj;
                    //String project = (String) jsonObject.get(projectId);
                    //this.projectIdName=project;
                    //System.out.println("projectId" + project);


                //System.out.println(messa);


                //queryResults.put(text, queryResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.listener.OnResult(responsemessage,1.0,"");

    }

    @Override
    public void endConversation() {
        init(config, log, "");

    }


//    public String getResponsemessage(){
//        return responsemessage;
//    }

    @Override
    public void close() {
        if (responsemessage == ""){
            new Timer().schedule(
                    new TimerTask(){

                        @Override
                        public void run(){
                            if (responsemessage == ""){
                                log.error("Error","No response from dialogflow");
                            }
                            //if you need some code to run when the delay expires
                        }
                    }, 5000);
        }

//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//            endTimes = System.currentTimeMillis();
//            duration = endTimes-startTimes;
            //System.out.println("result time"+duration);
//            if (responsemessage == ""){
//                log.error("Error","No response from dialogflow");
//            }


        }


    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {
        this.config =config;
        this.jsonCredential=config.get("credential");
        this.sessionId =config.get("sesssionID");
        this.languageCode = config.get("language");
        this.maximum = config.getAsInteger("maximum");
        //this.projectIdhaha=this.config.get("https://raw.githubusercontent.com/Sumethchan/credentials/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json");
        //this.projectIdhaha = config.get("Hello");
        this.log=log;
        //this.sessionsSettings = (SessionsSettings) extended_arg;
    }


}
