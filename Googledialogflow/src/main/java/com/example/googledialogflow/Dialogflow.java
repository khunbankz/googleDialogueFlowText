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
    private  String sessionId;
    private SessionsSettings sessionsSettings;
    private String languageCode;
    private String responsemessage;
    private ResultListener listener;
    private Config config;
    private Log log;
    private  int length;
    private int maximum;
    private int timeoutsession;
    private String messageInput;
    private String messageOld;
    private long startTimes;
    private long endTimes;
    private long duration;


    @Override
    public void open(ResultListener listener) {

        this.listener = listener;
        this.listener.OnResult(responsemessage,1.0,"");
    }

    @Override
    public void speech(String input) {

        this.startTimes = System.currentTimeMillis();
        length = input.length();
        if (length > maximum){
            log.error("Error:"," Too many characters input ");
            log.error("Error:","Your input have "+length+" characters");
            log.error("Error:","Maximun input is "+maximum+" characters");
        }
        List<String> texts = new ArrayList<>();
        texts.add(input);
        try(SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)){
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());
            this.log.debug(projectId,sessionId);
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
                System.out.println("sessionId:"+sessionId);



                message = queryResult.getFulfillmentText();

                this.responsemessage = message;
                this.log.info(message, String.valueOf(queryResult.getIntentDetectionConfidence()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void endConversation() {
        init(config, log, "");

    }
    @Override
    public void close() {
        if (responsemessage == ""){
            new Timer().schedule(
                    new TimerTask(){

                        @Override
                        public void run(){
                            if (responsemessage == ""){
                                log.error(sessionId,"No response from dialogflow");
                            }
                            //if you need some code to run when the delay expires
                        }
                    }, 5000);
        }

        }


    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {
        this.config =config;
        //this.jsonCredential=config.get("credential");
        this.sessionId =config.get("sesssionID");
        this.languageCode = config.get("language");
        this.maximum = config.getAsInteger("maximum");
        this.timeoutsession = config.getAsInteger("timeout");
        this.log=log;
        if(jsonCredential==null){
            this.jsonCredential=config.get("credential");
            InputStream stream = new ByteArrayInputStream(jsonCredential.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credentials = null;
            try {
                credentials = GoogleCredentials.fromStream(stream);
                projectId = ((ServiceAccountCredentials)credentials).getProjectId();
                SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
                settingsBuilder
                        .detectIntentSettings()
                        .setRetrySettings(
                                settingsBuilder
                                        .detectIntentSettings()
                                        .getRetrySettings()
                                        .toBuilder()
                                        .setTotalTimeout(Duration.ofSeconds(timeoutsession))
                                        .build());
                sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.sessionId = UUID.randomUUID().toString();


        }

    }


}
