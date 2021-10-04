package com.example.android_dialogflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dialogflow.DialogFactory;
import com.example.dialogflow.Test;
import com.example.dialogflow.TestDialogflow;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;

public class MainActivity extends AppCompatActivity {
    TestDialogflow dialogTest = new TestDialogflow();
    private SessionsSettings sessionsSettings;

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
        dialogTest.getSession(sessionsSettings);
        dialogTest.DetectIntentTexts("hi");

        dialogueInputText.speech("hi");

        Test test = new Test();
        String print = test.print();

        TextView requestTextview = findViewById(R.id.requestTextview);
//        requestTextview.setText("Request : " + print);
        requestTextview.setText("Request : " + dialogTest.input);

//        String result = TestDialogflow.detectIntentTexts();

        TextView responseTextview = findViewById(R.id.responseTextview);
        responseTextview.setText("Response : " + dialogTest.queryFullfillText );
    }

    public void initDialogflow() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            System.out.println("projectId (Android_Main) : " + projectId);

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            System.out.println("sessionsSettings : " + sessionsSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}