package com.example.dialogflow;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Maps;

import java.io.IOException;
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
    public String requestTextInput;
    public String queryFullfillText;
    private SessionsSettings sessionsSettings;
    private String sessionId;

    public void getSession(SessionsSettings sessionsSettings, String sessionId) {
        this.sessionsSettings = sessionsSettings;
        this.sessionId = sessionId;
        System.out.println("test123 : " + this.sessionsSettings);
    }

    @Override
    public void open(ResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void speech(String input) {
        List<String> text = new ArrayList<String>();
        Map<String, QueryResult> ret;
//        String sessionId = UUID.randomUUID().toString();

        requestTextInput = input;
        android.util.Log.d("DialogRequest", "sessionId: " + sessionId + " | request text: " + requestTextInput);
        text.add(input);
        ret = new HashMap<>();

        try {
            ret.putAll(detectIntentTexts("android-dialogflow-qrtf",
                    text, sessionId, "en"));

            queryFullfillText = ret.get(input).getFulfillmentText();
            System.out.format("Agent: %s\n", queryFullfillText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.format("End conver detect: '%s'\n", ret.get(input).getDiagnosticInfo().containsFields("end_conversation"));

//        this.listener.OnResult(queryFullfillText, 1.0, "");
        System.out.println(this.listener);

    }

    @Override
    public void close() {

    }

    @Override
    public void init(Config config, th.co.ais.genesis.blueprint.log.Log log, Object extended_arg) throws IllegalArgumentException {

    }

    public Map<String, QueryResult> detectIntentTexts(
            String projectId, List<String> texts, String sessionId, String languageCode)
            throws IOException, ApiException {
        Map<String, QueryResult> queryResults = Maps.newHashMap();

        System.out.println("sessionsSettings (module) " + sessionsSettings);

        SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
        SessionName session = SessionName.of(projectId, sessionId);
        System.out.println("Session Path: " + session.toString());

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
            //                System.out.format("%s", queryResult.getDiagnosticInfo());

            queryResults.put(text, queryResult);
        }

        return queryResults;
    }
}
