package com.example.dialogflow;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.Context;
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

//    private static final String TAG = String.format("DialogFlow %s", sessionId);
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

    private long startTimeMilli;
    private long endTimeMilli;

    public boolean timeReach;
    private String lastQueryFullfillText;

    private List<Context> queryContext;

//    private String checkedResult;

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

        boolean checkedInput = checkInput(requestTextInput);
        if (checkedInput == true) {
            //        android.util.Log.d("DialogRequest", "sessionId: " + sessionId  + " | request text: " + requestTextInput);
            this.log.info(TAG, "sessionId: " + sessionId  + " | request text: " + requestTextInput);
            text.add(input);
            requestMap = new HashMap<>();
            startTimeMilli = System.currentTimeMillis();

//            class count extends AsyncTask<Integer, Void, Boolean> {
//
//                @Override
//                protected Boolean doInBackground(Integer... integers) {
//                    return null;
//                }
//            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    timeReach = false;
                    startTimeMilli = System.currentTimeMillis();
                    System.out.println(String.valueOf(startTimeMilli));
                    while (true) {
                        if (System.currentTimeMillis() > startTimeMilli+5000) {
                            System.out.println(String.format("Timeout Reach"));
                            timeReach = true;
                            break;
                        }
//                        else {
//                            timeReach = false;
//                            System.out.println("PASS");
//                            break;
//                        }
                    }
                }
            });
//            thread.start();

            try {
                requestMap.putAll(detectIntentTexts(projectId, sessionId, text, languageCode));
//                checkResponseTime();

                queryFullfillText = requestMap.get(input).getFulfillmentText();
                queryConfidenceScore = requestMap.get(input).getIntentDetectionConfidence();
                queryContext = requestMap.get(input).getOutputContextsList();
                System.out.println(queryContext);

                //            System.out.format("Agent: %s\n", queryFullfillText);
                this.log.info(TAG, "Agent Response: " + queryFullfillText);
                //            System.out.format("Confidence Score: %.1f\n", queryConfidenceScore);
                this.log.info(TAG, "Confidence Score: " + queryConfidenceScore);

                this.listener.OnResult(queryFullfillText, queryConfidenceScore, "");
                endTimeMilli = System.currentTimeMillis();
                this.log.debug(TAG, String.format("Response Time = %d", endTimeMilli-startTimeMilli));

                this.log.info(TAG, "End conver detect: " + requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation"));
                if (requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation") == true) {
                    this.log.debug(TAG, "END DETECTED");
                    init(this.config, this.log, this.credentials_test_google);
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.log.warn(TAG, e);
                System.out.println("!!!!!!!");
            }

            System.out.println(lastQueryFullfillText);
//            System.out.println(timeReach);
//            if (timeReach == true && queryFullfillText == lastQueryFullfillText) {
//                lastQueryFullfillText = queryFullfillText;
//                this.log.error(TAG, "TIMEOUT");
//            } else lastQueryFullfillText = queryFullfillText;

            if (lastQueryFullfillText != queryFullfillText) {
                this.log.debug(TAG, "PASS");
                lastQueryFullfillText = queryFullfillText;
            }
            else if (queryFullfillText == lastQueryFullfillText){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (queryFullfillText == lastQueryFullfillText) {
                    lastQueryFullfillText = queryFullfillText;
                    this.log.error(TAG, "TIMEOUT : no response received");
                }
            }
//            else if (System.currentTimeMillis() > startTimeMilli+5000 & queryFullfillText == lastQueryFullfillText){
//                lastQueryFullfillText = queryFullfillText;
//                this.log.error(TAG, "TIMEOUT");
//            }

            //            else {
//                lastQueryFullfillText = queryFullfillText;
//                this.log.debug(TAG, "PASS");
//            }

            //        System.out.format("End conver detect: '%s'\n", requestMap.get(input).getDiagnosticInfo().containsFields("end_conversation"));
//            this.log.info(TAG, String.format("Context: %s", requestMap.get(input).getOutputContextsCount() > 0
//                    ? requestMap.get(input).getOutputContexts(0)
//                    : "no context"));
        } else {
            this.log.error(TAG, "Input does not meet Dialogflow requirement");
        }
    }

    private boolean checkInput(String text) {
        boolean checkedInput;
        int maxInputLength = this.config.getAsInteger("maxInputLength");
        int countText = text.length();
        this.log.debug(TAG, String.format("Input text Length = %d", countText));

        if (text.isEmpty() || text.equals(" ")) {
            checkedInput = false;
            this.log.error(TAG, "Please input text");
        }
        if (text.length() > 0 && !text.equals(" "))
            if (text.length() <= maxInputLength) checkedInput = true;
            else {
                checkedInput = false;
                this.log.error(TAG, String.format("Your input is %d, the maximum limit is %d", countText, maxInputLength));
            }
        else checkedInput = false;
        return checkedInput;
    }

    private boolean checkResponseTime () {
        startTimeMilli = System.currentTimeMillis();
        this.log.debug(TAG, String.valueOf(startTimeMilli));
        while (true) {
            if (System.currentTimeMillis() > startTimeMilli+5000 && queryFullfillText == null) {
                this.log.error(TAG, String.format("Timeout"));
                break;
            }
            if (queryFullfillText != null) {
                this.log.debug(TAG, "PASS");
                break;
            }
        }
        return true;
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
//        System.out.println("credentials_test : " + credentials_test);
        InputStream cred_stream = new ByteArrayInputStream(credentials_test.getBytes(StandardCharsets.UTF_8));
        try {
            credentials_test_google = GoogleCredentials.fromStream(cred_stream);
//            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
//            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials_test_google)).build();
//            sessionId = UUID.randomUUID().toString();
//            session = SessionName.of(projectId, sessionId);
////        SessionName session = SessionName.of(projectId, sessionId);
//            sessionsClient = SessionsClient.create(sessionsSettings);
        } catch (IOException e) {
            e.printStackTrace();
            this.log.warn(TAG, e);
            System.out.println("!!!!!!!");
        }

        this.log.debug(TAG, "credentials_test_google : " + credentials_test_google);
//        projectId = ((ServiceAccountCredentials)credentials).getProjectId();
        projectId = ((ServiceAccountCredentials)credentials_test_google).getProjectId();
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
            this.log.warn(TAG, e);
        }
        this.log.debug(TAG, "Session Path: " + session.toString());
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
//            TextInput.Builder textInput =
//                    TextInput.newBuilder().setField(Context context, queryContext);

            // Build the query with the TextInput
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
//            QueryInput queryInput1 = QueryInput.newBuilder().;
            System.out.println(queryInput);

            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            System.out.println(response);

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