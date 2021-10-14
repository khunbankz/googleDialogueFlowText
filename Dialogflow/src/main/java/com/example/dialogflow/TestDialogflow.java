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

public class TestDialogflow {
//    private SessionsClient sessionsClient;
//    private SessionName session;
    private SessionsSettings sessionsSettings;
    public String queryFullfillText;
    public String requestTextInput;

    public void getSession(SessionsSettings sessionsSettings) {
        this.sessionsSettings =  sessionsSettings;
    }

    public void DetectIntentTexts (String input) {
        this.requestTextInput = input;
        List<String> text = new ArrayList<String>();
//        String input;
        Map<String, QueryResult> ret;
        //sessionId is a suitable number for testing.
//        int sessionId = 123456789;
        String sessionId = UUID.randomUUID().toString();

//        do {
//            System.out.print("\nInput  : ");
//            input = userInput.nextLine();
//            if(input.equals("exit"))
//                System.exit(0);

        System.out.println("input : " + input);
        text.add(input);
//        System.out.println(text);
        ret = new HashMap<>();

        try {
//                //In the argument---Is the project described in the downloaded json file_Please replace with id.
            ret.putAll(detectIntentTexts("android-dialogflow-qrtf",
                    text, sessionId, "en"));

            queryFullfillText = ret.get(input).getFulfillmentText();
            System.out.format("Agent: %s\n", queryFullfillText);
//                System.out.format("test : %s", output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.format("End conver detect: '%s'\n", ret.get(input).getDiagnosticInfo().containsFields("end_conversation"));
    }

    public Map<String, QueryResult> detectIntentTexts(
            String projectId, List<String> texts, String sessionId, String languageCode)
            throws IOException, ApiException {
        Map<String, QueryResult> queryResults = Maps.newHashMap();

        System.out.println("sessionsSettings (module) " + sessionsSettings);

        SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
//            String sessionId = "123456789";
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
// [END dialogflow_detect_intent_text]
}
