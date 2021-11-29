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

    //String url = "https://github.com/Sumethchan/credentials/blob/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json";

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.messageinput);
        button = findViewById(R.id.submitbutton);
        TextView showhellotextview = findViewById(R.id.response);

        //new getNames().execute();
//        Handler handler = new Handler();
//        String jsonString = handler.httpServiceCall(url);
//        System.out.println("jsonString:"+jsonString);
//        class fetchData extends Thread{
//            String data = "";
//            URL url;
//            @Override
//            public void run() {
////                try {
////                    URL url = new URL("https://github.com/Sumethchan/credentials/blob/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json");
////                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
////                    InputStream inputStream = httpURLConnection.getInputStream();
////                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
////                    StringBuilder stringBuilder = new StringBuilder();
////                    String line;
////                    while ((line = bufferedReader.readLine()) != null){
////                        data = data +line;
////
////                    }if(!data.isEmpty()){
////                        //System.out.println(data);
//////                        JSONObject jsonObject = new JSONObject((Map) bufferedReader);
//////                        //JSONObject users = jsonObject.get("project_id");
//////                        String project = (String) jsonObject.get("project_id");
//////                        System.out.println("projectID name: "+project);
////                    }
////                } catch (MalformedURLException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//        }

        //setContentView(R.layout.activity_main);
        //TextView showhellotextview = findViewById(R.id.response);
        //TextView topic = findViewById(R.id.topic);
        //topic.setText("Input Text here");

        DialogueInputText dialogueInputText = new Dialogflowfactory().create();
        Config config = new MyConfig();
        //MyConfig config1 = new MyConfig();

        //InputStream inputStream = getAssets().open();
        //dialogueInputText.init(new MyConfig(),new MyLog(),this);

        //SessionsSettings sessionsSettings;
//        try {
//            InputStream stream = getResources().openRawResource(R.raw.application_default_credentials);
//            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
//            System.out.println("credential: "+credentials);
//            projectId = ((ServiceAccountCredentials)credentials).getProjectId();
//            sessionId = UUID.randomUUID().toString();
//            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
//            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        dialogueInputText.init(new MyConfig(),new MyLog(),sessionsSettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = input.getText().toString();
                //length = message.length();

                //System.out.println("lenght: "+length);
                //Dialogflow con = new Dialogflow();
                //con.configId(projectId,sessionId,sessionsSettings);
                //dialogueInputText.speech(message);
                //con.speech(message);
                //Dialogflowfactory dialogflowfactory = new Dialogflowfactory();
                //dialogflowfactory.create().speech(message);
                //config.get(projectId);
                //System.out.println("Idname"+config.get("hello"));
                long startTimes = System.currentTimeMillis();

                dialogueInputText.speech(message);
                dialogueInputText.open(new ResultListener() {
                    @Override
                    public void OnResult(String text, double confident, String more) {
                        showhellotextview.setText(text);
                       long endTimes = System.currentTimeMillis();
                       long duration = endTimes-startTimes;
                       System.out.println("response time: "+ duration);
//                       dialogueInputText.close();

                        //System.out.println("Response:"+text);
                    }
                });
//                long endTimes = System.currentTimeMillis();
//                long duration = endTimes-startTimes;
//                System.out.println("response time: "+ duration);
                dialogueInputText.close();


                //System.out.println("response time: "+ duration);

                //showhellotextview.setText();

                //System.out.println("message"+dialogflowfactory.getressponse());
                //showhellotextview.setText(con.getResponsemessage());
                //showhellotextview.setText(message);
            }
        });
        //Log.i("Android LifeCycle","onCreate");

    }
//    static class getNames extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            Handler handler = new Handler();
////            String jsonString = handler.httpServiceCall(url);
//            String result = null;
//            String urlJson = "https://raw.githubusercontent.com/Sumethchan/credentials/679cf3d865edca3f6c5a0aeb9e2c9a56974d975a/application_default_credentials.json";
//            try {
//                System.out.println("requestUrl: "+urlJson);
//                URL url = new URL(urlJson);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                //InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
////            JSONParser parser = new JSONParser();
////            Object obj = parser.parse(bufferedReader);
////            JSONObject jsonObject = (JSONObject) obj;
////            String project = (String) jsonObject.get("project_id");
////            System.out.println("projectIDname"+project);
//                String stringBuffer;
//                String string = "";
//                while ((stringBuffer = bufferedReader.readLine()) != null){
//                    string = String.format("%s%s", string, stringBuffer);
//                }
//                bufferedReader.close();
//                result = string;
//                //result = convertResultToString(inputStream);
//                System.out.println("result"+result);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }catch (ProtocolException e){
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //JSONObject jsonObject = new JSONObject(jsonString)
//            //System.out.println("jsonString: "+result);
//            return null;
////            if(jsonString != null){
////                try {
////                    JSONObject jsonObject = new JSONObject(jsonString);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
//
//        }
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//       // Log.i("Android LifeCycle","onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //Log.i("Android LifeCycle","onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //Log.i("Android LifeCycle","onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //Log.i("Android LifeCycle","onDestroy");
//    }
}