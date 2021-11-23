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

    private static final String TAG = DialogFlow.class.getSimpleName();
//    private static final String TAG_detectIntentTexts_method = "detectIntentTexts";

    private String requestTextInput;
    private String queryText;
    private String queryFullfillText;
    private double queryConfidenceScore;

    private String responseId;

    private String credentials;

    private SessionName session;
//    private ContextName context;
    private SessionsClient sessionsClient;
    private String sessionId;
    private String projectId;
    private String languageCode;

    private long startTimeMilli;
    private long endTimeMilli;

    private List<Context> queryContextList;
    private Context queryContext;

    @Override
    public void open(ResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void speech(String inputText) {
        List<String> text = new ArrayList<>();
        Map<String, QueryResult> requestMap;

        this.log.info(TAG + " sessionId = " + sessionId, "Input text: " + inputText);

        boolean checkedInput = checkInput(inputText);
        if (checkedInput == true) {
            this.log.info(TAG + " sessionId = " + sessionId, "request text: " + requestTextInput);
            text.add(requestTextInput);
            requestMap = new HashMap<>();
            startTimeMilli = System.currentTimeMillis();

            try {
                requestMap.putAll(detectIntentTexts(text, languageCode));

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

                this.log.debug(TAG, String.valueOf(requestMap.get(requestTextInput).getParameters()));
                this.log.debug(TAG, String.valueOf(requestMap.get(requestTextInput).getParameters().getFieldsMap()));
                this.log.debug(TAG, String.valueOf(requestMap.get(requestTextInput).getParameters().getFieldsMap().keySet()));

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

            } catch (Exception e) {
                e.printStackTrace();
                this.log.error(TAG, e);
                this.log.error(TAG + " sessionId = " + sessionId, "Check connection");
                this.listener.OnResult("Error : Check connection", 0.0, "");
                System.out.println("speech !!!!!!!");
            }

        }
        else {
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

        this.log.debug(TAG, "init invoked");
        if (credentials == null) {
            credentials = this.config.get("getCredentials");    //String
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
                this.listener.OnResult("ERROR GETTING CREDENTIAL\nCheck connection", 0.0, "");
            }
        }

        else {
            this.log.info(TAG, "projectId (DialogFlow_old) : " + projectId);

            sessionId = UUID.randomUUID().toString();
            this.log.info(TAG, "sessionId (DialogFlow_old) : " + sessionId);

            session = SessionName.of(projectId, sessionId);
            this.log.debug(TAG, "Session Path: " + session.toString());

            this.languageCode = this.config.get("languageCode");
            this.log.info(TAG, "languageCode : " + languageCode);
        }
    }

    public Map<String, QueryResult> detectIntentTexts(List<String> texts, String languageCode)  // sessionId from string/session cred
            throws ApiException {
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
        boolean checkedInput;
        int maxInputLength = this.config.getAsInteger("maxInputLength");
        this.log.debug(TAG + " sessionId = " + sessionId, String.format("Input text Length = %d", text.length()));

        if (text.startsWith(" ") || text.endsWith(" ")) {
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
        requestTextInput = text;
        return checkedInput;
    }

}