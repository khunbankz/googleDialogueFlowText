package com.example.myapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.googledialogflow.HttpManager;
import com.example.googledialogflow.Dialogflowfactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;
import th.co.ais.genesis.blueprint.log.Log;
//import android.util.Log;
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.ViewModelProviders;
//import com.example.dialogflowapplication.databinding.ActivityMainBinding;
//import com.getstream.sdk.chat.StreamChat;
//import com.getstream.sdk.chat.enums.FilterObject;
//import com.getstream.sdk.chat.rest.User;
//import com.getstream.sdk.chat.rest.core.Client;
//import com.getstream.sdk.chat.viewmodel.ChannelListViewModel;
//import java.util.HashMap;
//import static com.getstream.sdk.chat.enums.Filters.eq;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import java.util.Timer;

import org.json.simple.JSONObject;

public class MainActivity extends AppCompatActivity {
    String message;
    EditText input;
    Button button;
    String projectId;
    String sessionId;
    SessionsSettings sessionsSettings;
    Config config;
    Log log;
    String jsonStringCre;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.messageinput);
        button = findViewById(R.id.submitbutton);
        TextView showhellotextview = findViewById(R.id.response);



        DialogueInputText dialogueInputText = new Dialogflowfactory().create();
        Config config = new MyConfig();

        dialogueInputText.init(new MyConfig(),new MyLog(),sessionsSettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = input.getText().toString();

                long startTimes = System.currentTimeMillis();

                dialogueInputText.speech(message);
                dialogueInputText.open(new ResultListener() {
                    @Override
                    public void OnResult(String text, double confident, String more) {
                        showhellotextview.setText(text);
                       long endTimes = System.currentTimeMillis();
                       long duration = endTimes-startTimes;
                       System.out.println("response time: "+ duration);

                    }
                });

                dialogueInputText.close();


            }
        });


    }

}