package com.example.android_dialogflow;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dialogflow.DialogFactory;

import java.util.ArrayList;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;

public class MainActivity extends AppCompatActivity {
    private String resultResponse;
    private double resultConfident;

    DialogueInputText dialogueInputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> otherConfig = new ArrayList<>();

        TextView requestTextview = findViewById(R.id.requestTextview);
        TextView responseTextview = findViewById(R.id.responseTextview);

        dialogueInputText = new DialogFactory().create();
        dialogueInputText.open(new ResultListener() {
            @Override
            public void OnResult(String text, double confident, String more) {
                resultResponse = text;
                resultConfident = confident;
                Log.d("Android_Main", "message = " + resultResponse + ", confidence = " + resultConfident);
            }
        });
        dialogueInputText.init(new MyConfig(), new MyLog(), otherConfig);         //ori

        requestTextview.setText(resultResponse);
        responseTextview.setText("");

        EditText txtName = findViewById(R.id.textInput);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textInput = txtName.getText().toString();
                Log.d("Android_Main", "Input text : " + textInput);

                dialogueInputText.speech(textInput);
                requestTextview.setText("Request : " + textInput);
                responseTextview.setText("Response : " + resultResponse + "\nConfidence score : " + resultConfident);

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
}