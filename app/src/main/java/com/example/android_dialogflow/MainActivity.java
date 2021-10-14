package com.example.android_dialogflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dialogflow.DialogFactory;
import com.example.dialogflow.DialogFlow;
import com.example.dialogflow.Test;
import com.example.dialogflow.TestDialogflow;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;

public class MainActivity extends AppCompatActivity {
    private SessionsSettings sessionsSettings;
    private String sessionId;

//    TestDialogflow dialogTest = new TestDialogflow();
    DialogFlow dialogFlow = new DialogFlow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> otherConfig = new ArrayList<>();
        DialogueInputText dialogueInputText= new DialogFactory().create();
        dialogueInputText.init(new MyConfig(), new MyLog(), otherConfig);

        dialogueInputText.open(new ResultListener() {
            @Override
            public void OnResult(String text, double confident, String more) {
                Log.d("Hello", "message = " + text + ", confidence = " + confident);
            }
        });

        initDialogflow();

//        dialogTest.getSession(sessionsSettings);
//                dialogTest.DetectIntentTexts("hi");
        dialogFlow.getSession(sessionsSettings, sessionId);

        EditText txtname = findViewById(R.id.textInput);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textInput = txtname.getText().toString();
//                result.setText("Name:\t" + name + "\nPassword:\t" + password );
                Log.d("text", "Input text : " + textInput);

//                dialogueInputText.speech("hi");
                dialogFlow.speech(textInput);

                //        Test test = new Test();
//        String print = test.print();

                TextView requestTextview = findViewById(R.id.requestTextview);
//        requestTextview.setText("Request : " + print);
                requestTextview.setText("Request : " + dialogFlow.requestTextInput);

//        String result = TestDialogflow.detectIntentTexts();

                TextView responseTextview = findViewById(R.id.responseTextview);
                responseTextview.setText("Response : " + dialogFlow.queryFullfillText );

            }
        });
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
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            System.out.println("projectId (Android_Main) : " + projectId);

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionId = UUID.randomUUID().toString();
//            session = SessionName.of(projectId, sessionId);
            System.out.println("sessionsSettings : " + sessionsSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}