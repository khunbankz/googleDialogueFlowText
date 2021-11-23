package com.example.dialogflow;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.retrying.RetrySettings;
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

import org.threeten.bp.Duration;

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
    private static final String TAG = DialogFlow.class.getSimpleName();
    private static final String TAG_detectIntentTexts_method = "detectIntentTexts";

    private String requestTextInput;
//    private String lastRequestTextInput;
    private String queryText;
    private String queryFullfillText;
//    private String lastQueryFullfillText;
    private double queryConfidenceScore;

    private String responseId;
    private String lastResponseId;

    private String credentials;
//    private GoogleCredentials credentials_google;
//    private GoogleCredentials credentials;

    private SessionName session;
//    private ContextName context;
    private SessionsClient sessionsClient;
    private String sessionId;
    private String projectId;
//    private String projectId = "android-dialogflow-th-srfk";
//    private String projectId = "android-dialogflow-qrtf";
    private String languageCode;

    private long startTimeMilli;
    private long endTimeMilli;

    private List<Context> queryContextList;
    private Context queryContext;

    @Override
    public void open(ResultListener listener) {
        this.listener = listener;
//        listener.OnResult();
    }

    @Override
    public void speech(String inputText) {
        List<String> text = new ArrayList<String>();
        Map<String, QueryResult> requestMap;

//        requestTextInput = input;
        this.log.info(TAG + " sessionId = " + sessionId, "Input text: " + inputText);

        boolean checkedInput = checkInput(inputText);
        if (checkedInput == true) {
            this.log.info(TAG + " sessionId = " + sessionId, "request text: " + requestTextInput);
            text.add(requestTextInput);
            requestMap = new HashMap<>();
            startTimeMilli = System.currentTimeMillis();

            try {
                requestMap.putAll(detectIntentTexts(projectId, sessionId, text, languageCode));
//                checkResponseTime();

                queryText = requestMap.get(requestTextInput).getQueryText();
                queryFullfillText = requestMap.get(requestTextInput).getFulfillmentText();
                queryConfidenceScore = requestMap.get(requestTextInput).getIntentDetectionConfidence();
                queryContextList = requestMap.get(requestTextInput).getOutputContextsList();
                System.out.println("queryContextList\n"+queryContextList);
                if (!queryContextList.isEmpty()) {
//                    requestMap.get(inputText);
                    queryContext = requestMap.get(inputText).getOutputContexts(0);
                    System.out.println("queryContext = " + queryContext);
                }

                this.log.info(TAG + " sessionId = " + sessionId, "Query Text: " + queryText);
                this.log.info(TAG + " sessionId = " + sessionId, "Agent Response: " + queryFullfillText);
                this.log.info(TAG + " sessionId = " + sessionId, "Confidence Score: " + queryConfidenceScore);

                this.listener.OnResult(queryFullfillText, queryConfidenceScore, "");
                endTimeMilli = System.currentTimeMillis();
                this.log.debug(TAG + " sessionId = " + sessionId, String.format("Response Time = %d", endTimeMilli-startTimeMilli));

                this.log.debug(TAG + " sessionId = " + sessionId, "End conversation detect: " + requestMap.get(requestTextInput).getDiagnosticInfo().containsFields("end_conversation"));
                if (requestMap.get(requestTextInput).getDiagnosticInfo().containsFields("end_conversation") == true) {
                    this.log.debug(TAG + " sessionId = " + sessionId, "END DETECTED");
                    endConversation();
                }

//            } catch (InterruptedException | IOException e) {
            } catch (Exception e) {
                e.printStackTrace();
//                lastResponseId = responseId;
                this.log.error(TAG, e);
                this.log.error(TAG + " sessionId = " + sessionId, "Check connection");
                this.listener.OnResult("Error : Check connection", 0.0, "");
                System.out.println("speech !!!!!!!");
            }

//            this.log.debug(TAG, "lastResponseId = " + lastResponseId);
//            lastResponseId = responseId;

//            if (lastRequestTextInput != requestTextInput && lastResponseId != responseId) {
//            if (lastResponseId != responseId) {
//                this.log.debug(TAG, "Received response from Dialogflow ");
////                lastRequestTextInput = requestTextInput;
//                lastResponseId = responseId;
//            }
//            else if (lastResponseId == responseId){
//                int waitTime = this.config.getAsInteger("maxResponseTime");
//                try {
//                    Thread.sleep(waitTime);
////                    speech(requestTextInput);
////                    lastResponseId = responseId;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (lastResponseId == responseId) {
////                    lastRequestTextInput = requestTextInput;
//                    lastResponseId = responseId;
//                    this.log.error(TAG, "TIMEOUT : no response received");
//                    this.listener.OnResult("TIMEOUT : no response received", 0.0, "");
//                } else {
//                    this.log.debug(TAG, "Received response from Dialogflow ");
//                }
//            }
//            else this.log.warn(TAG, "CHECK checkResponse");
        } else {
            this.log.error(TAG, "Input does not meet Dialogflow requirement");
            this.listener.OnResult("Input does not meet Dialogflow requirement", 0.0, "");
        }
    }

    @Override
    public void endConversation() {
        init(config, log, "");
    }

    @Override
    public void close() {

    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {
        this.log = log;
        this.config = config;
        int maxResponseTime = this.config.getAsInteger("maxResponseTime");
//        this.sessionsSettings = (SessionsSettings) extended_arg;
//        this.credentials = (GoogleCredentials) extended_arg;

        this.log.debug(TAG, "init invoked");
        if (credentials == null) {
            credentials = this.config.get("getCredentials");          //String
//            this.log.debug(TAG, "credentials_test : " + credentials);
            try {
                InputStream credStream = new ByteArrayInputStream(credentials.getBytes(StandardCharsets.UTF_8));
                GoogleCredentials credentialsGoogle = GoogleCredentials.fromStream(credStream);       //GoogleCred
                this.log.debug(TAG, "credentials_google : " + credentialsGoogle);

                SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
                sessionsSettingsBuilder
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentialsGoogle))
                        .detectIntentSettings()
                        .setRetrySettings(RetrySettings.newBuilder()
                                .setTotalTimeout(Duration.ofSeconds(maxResponseTime)).build());     //set max response time
                SessionsSettings sessionsSettings = sessionsSettingsBuilder.build();
                sessionsClient = SessionsClient.create(sessionsSettings);

                projectId = ((ServiceAccountCredentials) credentialsGoogle).getProjectId();
                this.log.info(TAG, "projectId (DialogFlow_new) : " + projectId);

                sessionId = UUID.randomUUID().toString();
                this.log.info(TAG, "sessionId (DialogFlow_new) : " + sessionId);

                session = SessionName.of(projectId, sessionId);
                this.log.debug(TAG, "Session Path: " + session.toString());

                this.languageCode = this.config.get("languageCode");
                this.log.info(TAG, "languageCode : " + languageCode);
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
                this.log.error(TAG, "init !!!!!!!");
                this.log.error(TAG, e);
//                this.listener.OnResult("(init) Error : Check connection", 0.0, "");        //check
                this.listener.OnResult("ERROR GETTING CREDENTIAL\n check connection", 0.0, "");
            }
        }
//        try {
//            if (credentials != null) {
//                InputStream credStream = new ByteArrayInputStream(credentials.getBytes(StandardCharsets.UTF_8));
//                GoogleCredentials credentials_google = GoogleCredentials.fromStream(credStream);       //GoogleCred
//                this.log.debug(TAG, "credentials_google : " + credentials_google);
//
//                SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
//                sessionsSettingsBuilder
//                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials_google))
//                        .detectIntentSettings()
//                        .setRetrySettings(RetrySettings.newBuilder()
//                                .setTotalTimeout(Duration.ofSeconds(maxResponseTime)).build());     //set max response time
//                SessionsSettings sessionsSettings = sessionsSettingsBuilder.build();
//                sessionsClient = SessionsClient.create(sessionsSettings);

//        projectId = ((ServiceAccountCredentials)credentials).getProjectId();
//                projectId = ((ServiceAccountCredentials)credentials_google).getProjectId();

        else {
            this.log.info(TAG, "projectId (DialogFlow_old) : " + projectId);

            sessionId = UUID.randomUUID().toString();
            this.log.info(TAG, "sessionId (DialogFlow_old) : " + sessionId);

            session = SessionName.of(projectId, sessionId);
            this.log.debug(TAG, "Session Path: " + session.toString());

            this.languageCode = this.config.get("languageCode");
            this.log.info(TAG, "languageCode : " + languageCode);
        }
//            }
//            else {
//                this.log.error(TAG, "(init) Error : Check connection");
////                checkCred();
//                this.listener.OnResult("ERROR GETTING CREDENTIAL\n check connection", 0.0, "");
//            }
        }
//        catch (Exception e) {
//            this.listener.OnResult("(init) Error : Check connection", 0.0, "");        //check
////            e.printStackTrace();
//            this.log.error(TAG, "init !!!!!!!");
//            this.log.error(TAG, e);
//        }
//    }

//    private void checkCred() {
//        this.listener.OnResult("ERROR GETTING CREDENTIAL\n check connection", 0.0, "");
//    }

    public Map<String, QueryResult> detectIntentTexts(
            String projectId, String sessionId, List<String> texts, String languageCode)  // sessionId from string/session cred
            throws IOException, ApiException, InterruptedException {
        Map<String, QueryResult> queryResults = Maps.newHashMap();

        for (String text : texts) {
            TextInput.Builder textInput =
                    TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            this.log.debug(TAG + " sessionId = " + sessionId, "sessionsClient " + sessionsClient);
            this.log.debug(TAG + " sessionId = " + sessionId, "response " + response);

            responseId = response.getResponseId();
            this.log.debug(TAG + " sessionId = " + sessionId, "responseId " + responseId);

            QueryResult queryResult = response.getQueryResult();
            queryResults.put(text, queryResult);
        }

        return queryResults;
    }

    private boolean checkInput(String text) {
        boolean checkedInput = false;
        int maxInputLength = this.config.getAsInteger("maxInputLength");
        this.log.debug(TAG + " sessionId = " + sessionId, String.format("Input text Length = %d", text.length()));

//        if (text.startsWith(" ") || text.endsWith(" ")) {
//            text = text.trim();
//            this.log.debug(TAG + " sessionId = " + sessionId, String.format("Trim text Length = %d", text.length()));
//        }
        if (Character.isWhitespace(text.charAt(0))) {
            text = text.trim();
            this.log.debug(TAG + " sessionId = " + sessionId, String.format("Trim text Length = %d", text.length()));
        }

        String checkNullText = String.valueOf(text.isEmpty());
        switch (checkNullText) {
            case "true":
                checkedInput = false;
                this.log.error(TAG + " sessionId = " + sessionId + " sessionId = " + sessionId, "Please input text");
                break;
            case "false":
                if (text.length() <= maxInputLength) checkedInput = true;
                else {
                    checkedInput = false;
                    this.log.error(TAG + " sessionId = " + sessionId, String.format("Your input is %d, the maximum limit is %d", text.length(), maxInputLength));
                }
                break;
            default:
                this.log.error(TAG + " sessionId = " + sessionId, "CHECK checkInput");
                throw new IllegalStateException("Unexpected value: " + text);
        }

//        if (text.isEmpty()) {
////            checkedInput = false;
////            this.log.error(TAG, "Please input text");
////        }
////        else if (text.length() > 0) {
////            if (text.length() <= maxInputLength) checkedInput = true;
//            else {
//                checkedInput = false;
//                this.log.error(TAG, String.format("Your input is %d, the maximum limit is %d", countText, maxInputLength));
//            }
//        }
//        else this.log.error(TAG, "CHECK checkInput");
        requestTextInput = text;
        return checkedInput;
    }

}