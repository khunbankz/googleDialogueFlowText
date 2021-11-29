package com.example.googledialogflow;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.concurrent.TimeUnit;

public class DetectIntentTexts {
    public static void main(String[] args) throws IOException {
        boolean check = true;

        while (check) {
            System.out.printf("Enter you request: ");

            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            List<String> texts = new ArrayList<>();
            //texts.add("หวัดดี");
            texts.add(s);
            long startTimes = System.currentTimeMillis();
            //InputStream in = context.getResources().openRawResource(R.raw.credentials);
            //GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            detectIntentTexts("submarine-chatbot-dsuq", texts, "452431082519", "th");
            //Fullfilment("submarine-chatbot-dsuq", texts, "452431082519", "th");
            long endTimes = System.currentTimeMillis();
            long duration = endTimes-startTimes;
            //long timeres =  TimeUnit.MILLISECONDS.toSeconds(duration);
            //System.out.println(timeres);
            System.out.println("response time : "+duration+" millisecond");
            //Fullfilment("submarine-chatbot-dsuq", texts, "452431082519", "th");

        }
    }

    // DialogFlow API Detect Intent sample with text inputs.
    public static Map<String, QueryResult> detectIntentTexts(
            String projectId, List<String> texts, String sessionId, String languageCode)
            throws IOException, ApiException {
        Map<String, QueryResult> queryResults = Maps.newHashMap();
        // Instantiates a client
        //(SessionsClient sessionsClient = SessionsClient.create())

        try(SessionsClient sessionsClient = SessionsClient.create()){
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
//                System.out.format(
//                        "Fulfillment Text: '%s'\n",
//                        queryResult.getFulfillmentMessagesCount() > 0
//                                ? queryResult.getFulfillmentMessages(0).getText()
//                                : "Triggered Default Fallback Intent");
                System.out.println("answer_response: "+queryResult.getFulfillmentText());
                //String messa = queryResult.getFulfillmentText();
                //System.out.println(messa);

                queryResults.put(text, queryResult);
            }
        }
        return queryResults;
    }
    public static String Fullfilment(String projectId, List<String> texts, String sessionId, String languageCode) throws IOException {
        String messa="";
        try(SessionsClient sessionsClient = SessionsClient.create()){
            SessionName session = SessionName.of(projectId, sessionId);
            //System.out.println("Session Path: " + session.toString());

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
//                System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
//                System.out.format(
//                        "Detected Intent: %s (confidence: %f)\n",
//                        queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
//                System.out.format(
//                        "Fulfillment Text: '%s'\n",
//                        queryResult.getFulfillmentMessagesCount() > 0
//                                ? queryResult.getFulfillmentMessages(0).getText()
//                                : "Triggered Default Fallback Intent");
                System.out.println("answer_response: "+queryResult.getFulfillmentText());
                messa = queryResult.getFulfillmentText();

            }
        }

        //return ;
        return messa;
    }
    public List<String> Scanmessage(){
        System.out.printf("Enter you request: ");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        List<String> texts = new ArrayList<>();
        //texts.add("หวัดดี");
        texts.add(s);
        return texts;

    }


}
