package com.example.android_dialogflow;

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

import java.io.InputStream;
import java.util.ArrayList;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;

//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {
    private SessionsSettings sessionsSettings;
    private GoogleCredentials credentials;
//    private String sessionId;
//    private SessionName session;

    private String resultResponse;
    private double resultConfident;

//    TestDialogflow dialogTest = new TestDialogflow();
//    DialogFlow dialogFlow = new DialogFlow();

//    final String credUrl =
//            "https://drive.google.com/uc?id=10edZa3D8Sw-AZYP6UMfekDHMEj_q2aEz";
//    private String result;

    DialogueInputText dialogueInputText;

//    private final OkHttpClient client = new OkHttpClient();
//
//    public void whenAsynchronousGetRequest_thenCorrect() {
//        Request request = new Request.Builder()
//                .url(credUrl)
//                .build();
//
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
//                System.out.println(response.body().string());
//            }
//
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                fail();
//            }
//        });
//    }

//    private void getCred() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(1, TimeUnit.MINUTES)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://drive.google.com/")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
////                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//        System.out.println(retrofit);
//
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//
////        Call call = client.newCall(request);
////        call.enqueue(new Callback() {
////            public void onResponse(Call call, Response response) throws IOException {
////                try (ResponseBody responseBody = response.body()) {
////
////                }
////            }
////
////            public void onFailure(Call call, IOException e) {
////            }
//        Call<MyConfig> call = retrofitAPI.getCred();
////        MyConfig call = retrofitAPI.getCred(new MyConfig());
//
//        call.enqueue(new Callback<MyConfig>() {
//            @Override
//            public void onResponse(Call<MyConfig> call, Response<MyConfig> response) {
//                if (response.isSuccessful()) {
//                    System.out.println("x "+response.code());
////                    response.body().string();
//                    MyConfig modal = response.body();
//                    System.out.println(modal.getType());
//                    System.out.println(modal.getProjectId());
//                    System.out.println(modal.getClientId());
////                    System.out.println("x "+response.body().string());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyConfig> call, Throwable t) {
//                System.out.println("FAILED");
//
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        boolean connected;
//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//            //we are connected to a network
//            connected = true;
//        }
//        else
//            connected = false;
//
//        System.out.println("connection = " + connected);

//        initDialogflow();

//        MyConfig getConfig = new MyConfig();
//        getConfig.get("setCredentials");

        ArrayList<String> otherConfig = new ArrayList<>();
//        otherConfig.add(sessionsSettings);

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
////        dialogueInputText.init(new MyConfig(), new MyLog(), sessionsSettings);    //pass sessionsSettings
////        dialogueInputText.init(new MyConfig(), new MyLog(), credentials);           //pass credentials
//
        requestTextview.setText(resultResponse);
        responseTextview.setText("");
//
//        //        dialogTest.getSession(sessionsSettings);
////                dialogTest.DetectIntentTexts("hi");
////        dialogFlow.getSession(sessionsSettings, session, sessionId);
////        dialogFlow.getSession(sessionsSettings);


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

//                TextView requestTextview = findViewById(R.id.requestTextview);
//        requestTextview.setText("Request : " + print);
                requestTextview.setText("Request : " + textInput);

//        String result = TestDialogflow.detectIntentTexts();

//                TextView responseTextview = findViewById(R.id.responseTextview);
                responseTextview.setText("Response : " + resultResponse + "\nConfidence score : " + resultConfident);

            }
        });
    }

//    private class GetCredUrl extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            URL url;
//            try {
//                System.out.println("result test " + result);
//                url = new URL(credUrl);
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
//                String stringBuffer;
//                String string = "";
//                while ((stringBuffer = bufferedReader.readLine()) != null){
//                    string = String.format("%s%s", string, stringBuffer);
////                    check = true;
//                }
//                bufferedReader.close();
//                result = string;
//                System.out.println("result GetCredUrl " + result);
//
////                InputStream test = new InputStreamReader(url.openStream());
////                credentials = GoogleCredentials.fromStream(test);
//            } catch (IOException e){
//                e.printStackTrace();
//                result = e.toString();
//                System.out.println("result ex " + result);
//            }
//            return null;
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        dialogueInputText.open(new ResultListener() {
//            @Override
//            public void OnResult(String text, double confident, String more) {
//                resultResponse = text;
//                resultConfident = confident;
//                Log.d("Android_Main", "message = " + resultResponse + ", confidence = " + resultConfident);
//            }
//        });
////        dialogueInputText.speech("hello");
//
//        EditText txtname = findViewById(R.id.textInput);
//        Button buttonSubmit = findViewById(R.id.buttonSubmit);
//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String textInput = txtname.getText().toString();
////                result.setText("Name:\t" + name + "\nPassword:\t" + password );
//                Log.d("Android_Main", "Input text : " + textInput);
//
////                dialogueInputText.speech("hi");
////                dialogFlow.speech("textInput");
//                dialogueInputText.speech(textInput);
//
//                //        Test test = new Test();
////        String print = test.print();
//
//                TextView requestTextview = findViewById(R.id.requestTextview);
////        requestTextview.setText("Request : " + print);
//                requestTextview.setText("Request : " + textInput);
//
////        String result = TestDialogflow.detectIntentTexts();
//
//                TextView responseTextview = findViewById(R.id.responseTextview);
//                responseTextview.setText("Response : " + resultResponse + "\nConfidence score : " + resultConfident);
//
//            }
//        });
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

//    private void checkConnection() {
//
//        // initialize intent filter
//        IntentFilter intentFilter = new IntentFilter();
//
//        // add action
//        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");
//
//        // register receiver
//        registerReceiver(new ConnectionReceiver(), intentFilter);
//
//        // Initialize listener
//        ConnectionReceiver.Listener = (ConnectionReceiver.ReceiverListener) this;
//
//        // Initialize connectivity manager
//        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Initialize network info
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        // get connection status
//        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
//
//        System.out.println(isConnected);
//
//    }
//    public static boolean checkConnection(Context context) {
//        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connMgr != null) {
//            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
//
//            if (activeNetworkInfo != null) { // connected to the internet
//                // connected to the mobile provider's data plan
//                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                    // connected to wifi
//                    return true;
//                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
//            }
//        }
//        return false;
//    }
}