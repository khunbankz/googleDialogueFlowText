package com.example.dialogflow;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;
import th.co.ais.genesis.blueprint.log.Log;

public class DialogFlow implements DialogueInputText {
    private ResultListener listener;
    private Log log;
    private Config config;

    private static final String TAG = "DialogFlow";

    public String requestTextInput;
    public String queryFullfillText;
    public double queryConfidenceScore;

//    private SessionsSettings sessionsSettings;
    private String credentials_test;
    private GoogleCredentials credentials_test_google;

//    private GoogleCredentials credentials;
    private SessionName session;
    private SessionsClient sessionsClient;
    private String sessionId;
    private String projectId;
//    private String projectId = "android-dialogflow-th-srfk";
//    private String projectId = "android-dialogflow-qrtf";
    private String languageCode;

    private String checkedResult;

    @Override
    public void open(ResultListener listener) {
        this.listener = listener;
//        listener.OnResult();
    }

    @Override
    public void speech(String input) {
        List<String> text = new ArrayList<String>();
        Map<String, QueryResult> requestMap;
//        sessionId = UUID.randomUUID().toString();

        requestTextInput = input;

        String checkedInput = checkInput(requestTextInput);
        if (checkedInput != "not pass") {
            //        android.util.Log.d("DialogRequest", "sessionId: " + sessionId  + " | request text: " + requestTextInput);
            this.log.info(TAG, "sessionId: " + sessionId  + " | request text: " + requestTextInput);
            text.add(input);
            requestMap = new HashMap<>();

            try {
                requestMap.putAll(detectIntentTexts(projectId, sessionId, text, languageCode));

                queryFullfillText = requestMap.get(input).getFulfillmentText();
                queryConfidenceScore = requestMap.get(input).getIntentDetectionConfidence();

                //            System.out.format("Agent: %s\n", queryFullfillText);
                this.log.info(TAG, "Agent Response: " + queryFullfillText);
                //            System.out.format("Confidence Score: %.1f\n", queryConfidenceScore);
                this.log.info(TAG, "Confidence Score: " + queryConfidenceScore);

                this.listener.OnResult(queryFullfillText, queryConfidenceScore, "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //        System.out.format("End conver detect: '%s'\n", requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation"));
//            this.log.info(TAG, String.format("Context: %s", requestMap.get(input).getOutputContextsCount() > 0
//                    ? requestMap.get(input).getOutputContexts(0)
//                    : "no context"));
            this.log.info(TAG, "End conver detect: " + requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation"));
            if (requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation") == true) {
                this.log.debug(TAG, "END DETECTED");
                init(this.config, this.log, this.credentials_test_google);
            }
        } else {
            this.log.error(TAG, "Input text does not meet Dialogflow requirement");
        }
    }

    public String checkInput(String text) {
        int maxInputLength = Integer.parseInt(this.config.get("maxInputLength"));
        int countText = text.length();
        this.log.debug(TAG, String.format("Input text Length = %d", countText));

        if (text.isEmpty()) {
            checkedResult = "not pass";
            this.log.error(TAG, "Please input text");
        }
        if (text.length() > 0)
            if (text.length() <= maxInputLength) checkedResult = text;
            else {
                checkedResult = "not pass";
                this.log.error(TAG, String.format("Your input is %d, the maximum limit is %d", countText, maxInputLength));
            }
        else checkedResult = "not pass";
        return checkedResult;
    }

    @Override
    public void close() {

    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {
        this.log = log;
        this.config = config;
//        this.sessionsSettings = (SessionsSettings) extended_arg;
//        this.credentials = (GoogleCredentials) extended_arg;

        this.log.debug(TAG, "init invoked");

        credentials_test = this.config.get("getCredentials");
        System.out.println("credentials_test : " + credentials_test);
        InputStream stream = new ByteArrayInputStream(credentials_test.getBytes(StandardCharsets.UTF_8));
//        InputStream stream = new ByteArrayInputStream(credentials_test.getBytes());
//        System.out.println("1 " + stream);

//        GoogleCredentials credentials = GoogleCredentials.fromStream(credentials_test);
        try {
            credentials_test_google = GoogleCredentials.fromStream(stream);
            System.out.println("credentials_test_google : " + credentials_test_google);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        projectId = ((ServiceAccountCredentials)credentials).getProjectId();
        projectId = ((ServiceAccountCredentials)credentials_test_google).getProjectId();
//        System.out.println("projectId (DialogFlow_module) : " + projectId);
        this.log.info(TAG, "projectId (DialogFlow_module) : " + projectId);

        this.languageCode = this.config.get("languageCode");
        this.log.info(TAG, "languageCode : " + languageCode);

        try {
        SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
        SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials_test_google)).build();
        sessionId = UUID.randomUUID().toString();
        session = SessionName.of(projectId, sessionId);
//        SessionName session = SessionName.of(projectId, sessionId);
        sessionsClient = SessionsClient.create(sessionsSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Session Path: " + session.toString());
        this.log.debug(TAG, "Session Path: " + session.toString());

//        System.out.println("Config = " + config);
        this.log.debug(TAG, "Config = " + config);
    }

    public Map<String, QueryResult> detectIntentTexts(
            String projectId, String sessionId, List<String> texts, String languageCode)  //sessionId from string/ session cred
            throws IOException, ApiException {
        Map<String, QueryResult> queryResults = Maps.newHashMap();

//        this.log.debug(TAG, String.valueOf(sessionsSettings));
//        x.debug("test_debug","x");

        // Detect intents for each text input
        for (String text : texts) {
            // Set the text (hello) and language code (en-US) for the query
            TextInput.Builder textInput =
                    TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

            // Build the query with the TextInput
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            // Display the query result
            QueryResult queryResult = response.getQueryResult();

//            System.out.println("====================");
//            System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
//            System.out.format(
//                    "Detected Intent: %s (confidence: %f)\n",
//                    queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
//            System.out.format(
//                    "Fulfillment Text: '%s'\n",
//                    queryResult.getFulfillmentMessagesCount() > 0
//                            ? queryResult.getFulfillmentMessages(0).getText()
//                            : "Triggered Default Fallback Intent");
//            //                System.out.format("%s", queryResult.getDiagnosticInfo());
//            System.out.println("context : \n" + queryResult.getOutputContextsList());     //context

//            this.log.debug(TAG,"====================");
            this.log.info(TAG,"Query Text: " + queryResult.getQueryText());
            this.log.debug(TAG,"Detected Intent: " + queryResult.getIntent().getDisplayName());
            this.log.debug(TAG,"confidence: " + queryResult.getIntentDetectionConfidence());
            if (queryResult.getFulfillmentMessagesCount() > 0)
                this.log.debug(TAG,"Fulfillment Text: " +
                        queryResult.getFulfillmentMessages(0).getText());
            else this.log.debug(TAG,
                    "Triggered Default Fallback Intent");

            queryResults.put(text, queryResult);
        }

        return queryResults;
    }
}
