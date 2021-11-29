//package com.example.googledialogflow;//package com.example.googledialogflow;
//
//import android.content.res.Resources;
//import com.google.api.gax.core.FixedCredentialsProvider;
//import com.google.api.gax.rpc.ApiException;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.dialogflow.v2.DetectIntentResponse;
//import com.google.cloud.dialogflow.v2.QueryInput;
//import com.google.cloud.dialogflow.v2.QueryResult;
//import com.google.cloud.dialogflow.v2.SessionName;
//import com.google.cloud.dialogflow.v2.Context;
//import com.google.cloud.dialogflow.v2.SessionsSettings;
//import com.google.cloud.dialogflow.v2.TextInput;
//import com.google.cloud.dialogflow.v2.SessionsClient;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//
//import android.content.SharedPreferences;
//
//import android.util.Log;
//
//
//import javax.annotation.Resource;
//
//public class SendText {
//    public static void main(String[] args) throws IOException {
//        while (true) {
//            System.out.printf("Enter you request: ");
//            Scanner sc = new Scanner(System.in);
//            String s = sc.nextLine();
//            //List<String> texts = new ArrayList<>();
//            //texts.add("หวัดดี");
//
//            //texts.add(s);
//            detectIntentTexts("submarine-chatbot-dsuq", s, "452431082519", "th");
//        }
//    }
//    // DialogFlow API Detect Intent sample with text inputs.
//    public static Map<String, QueryResult> detectIntentTexts(
//            String projectId, String texts, String sessionId, String languageCode)
//            throws IOException, ApiException {
//        Map<String, QueryResult> queryResults = Maps.newHashMap();
//        // Instantiates a client
//        //(SessionsClient sessionsClient = SessionsClient.create())
//
//        try(SessionsClient sessionsClient = SessionsClient.create()){
//
////            InputStream stream = getResources().openRawResource(R.raw.credential);
////            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
////                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
////            //String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
////            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
////            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
////                    FixedCredentialsProvider.create(credentials)).build();
//            //SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
//            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
//            SessionName session = SessionName.of(projectId, sessionId);
//            System.out.println("Session Path: " + session.toString());
//
//            // Detect intents for each text input
//
//                // Set the text (hello) and language code (en-US) for the query
//                TextInput.Builder textInput =
//                        TextInput.newBuilder().setText(texts).setLanguageCode(languageCode);
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
////                System.out.format(
////                        "Fulfillment Text: '%s'\n",
////                        queryResult.getFulfillmentMessagesCount() > 0
////                                ? queryResult.getFulfillmentMessages(0).getText()
////                                : "Triggered Default Fallback Intent");
//                System.out.println("answer_response: "+queryResult.getFulfillmentText());
//
//                queryResults.put(texts, queryResult);
//
//        }
//        return queryResults;
//    }
//
//
//}
