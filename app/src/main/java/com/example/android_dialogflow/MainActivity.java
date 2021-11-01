package com.example.android_dialogflow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dialogflow.DialogFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;

public class MainActivity extends AppCompatActivity {
    private SessionsSettings sessionsSettings;
    private GoogleCredentials credentials;
//    private String sessionId;
//    private SessionName session;

    private String resultResponse;
    private double resultConfident;

//    TestDialogflow dialogTest = new TestDialogflow();
//    DialogFlow dialogFlow = new DialogFlow();

//    final String strMessage = "https://sites.google.com/site/androidersite/text.txt";
//    final String strMessage = "https://doc-0k-as-docs.googleusercontent.com/docs/securesc/9mumr0t6btu25dm723utun3577dma7eo/1ll1qts37c4hl1dstph920s6a9np4916/1635492600000/07219187782740445536/06797400023169037954Z/1d6uqRhFRu_ykQOP9u4ZFYxXsuzpTVA0q?e=download&nonce=33qf54mr18agc&user=06797400023169037954Z&hash=fq7qcs2pb2pk6erj5f8qnphqfdbqdprb";
//    final String strMessage = "https://raw.githubusercontent.com/Weerapat1455/And_cred/42ca2990841c96eb29bee1eec88cdfdbde28257e/credential.json";
    final String strMessage = "https://raw.githubusercontent.com/Weerapat1455/And_cred/main/credential.json?token=AOKNYQ2R5CTUBZWAQZE6DWDBPOUGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDialogflow();

//        JSONParser parser = new JSONParser();
//
//        try (Reader reader = new FileReader("/Users/terr/Documents/DataAI/Android/credential.json")) {
//
//            JSONObject jsonObject = (JSONObject) parser.parse(reader);
//            System.out.println(jsonObject);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        MyConfig getConfig = new MyConfig();
//        getConfig.get("setCredentials");

        ArrayList<String> otherConfig = new ArrayList<>();
//        otherConfig.add(sessionsSettings);

        DialogueInputText dialogueInputText = new DialogFactory().create();
//        dialogueInputText.init(new MyConfig(), new MyLog(), otherConfig);         //ori
//        dialogueInputText.init(new MyConfig(), new MyLog(), sessionsSettings);    //pass sessionsSettings
        dialogueInputText.init(new MyConfig(), new MyLog(), otherConfig);           //pass credentials


        //        dialogTest.getSession(sessionsSettings);
//                dialogTest.DetectIntentTexts("hi");
//        dialogFlow.getSession(sessionsSettings, session, sessionId);
//        dialogFlow.getSession(sessionsSettings);

//        new MyTask().execute();
//
//        class fetch extends Thread {
//            String data =  "";
//            @Override
//            public void run() {
//
//                ArrayList<String> nameList = null;
//
//                try {
//                    URL url = new URL("https://drive.google.com/file/d/1d6uqRhFRu_ykQOP9u4ZFYxXsuzpTVA0q/view?usp=sharing");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    InputStream stream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
////                    buffer.append(line + "\n");
//                        data = data+line;
//                        Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
//                    }
//                    if (!data.isEmpty()){
//                        JSONObject jsonObject = new JSONObject(data);
//                        JSONArray test = jsonObject.getJSONArray("project_id");
//                        JSONObject names = test.getJSONObject(0);
//                        String name = names.getString("project_id");
//                        nameList.add(name);
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
////        System.out.println("urls" + buffer.toString(););
//                System.out.println("urls" + data);
////        System.out.println("urls" + Arrays.toString(nameList));
//            }
//        }

        dialogueInputText.open(new ResultListener() {
            @Override
            public void OnResult(String text, double confident, String more) {
                resultResponse = text;
                resultConfident = confident;
                Log.d("Android_Main", "message = " + resultResponse + ", confidence = " + resultConfident);
            }
        });
//        dialogueInputText.speech("hello");

        EditText txtname = findViewById(R.id.textInput);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textInput = txtname.getText().toString();
//                result.setText("Name:\t" + name + "\nPassword:\t" + password );
                Log.d("Android_Main", "Input text : " + textInput);

//                dialogueInputText.speech("hi");
//                dialogFlow.speech("textInput");
                dialogueInputText.speech(textInput);

                //        Test test = new Test();
//        String print = test.print();

                TextView requestTextview = findViewById(R.id.requestTextview);
//        requestTextview.setText("Request : " + print);
                requestTextview.setText("Request : " + textInput);

//        String result = TestDialogflow.detectIntentTexts();

                TextView responseTextview = findViewById(R.id.responseTextview);
                responseTextview.setText("Response : " + resultResponse + "\nConfidence score : " + resultConfident);

            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            try {
                System.out.println("result test " + result);
                url = new URL(strMessage);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;
                System.out.println("result run " + result);
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
                System.out.println("result ex " + result);
            }
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDialogflow() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.credential);
            credentials = GoogleCredentials.fromStream(stream);
            System.out.println("credentials (Android_Main) = " + credentials);

//            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
//
//            System.out.println("projectId (Android_Main) : " + projectId);
//
//            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
//            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
//            sessionId = UUID.randomUUID().toString();
//            session = SessionName.of(projectId, sessionId);
//            System.out.println("sessionsSettings (Android_Main) : " + sessionsSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}