package com.example.android_dialogflow;

import static org.junit.Assert.fail;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.log.Log;

public class MyConfig implements Config {
    final String credUrl =
//            "https://raw.githubusercontent.com/Weerapat1455/And_cred/main/credential.json";
//            "https://drive.google.com/uc?id=1d6uqRhFRu_ykQOP9u4ZFYxXsuzpTVA0q";
            "https://drive.google.com/uc?id=10edZa3D8Sw-AZYP6UMfekDHMEj_q2aEz";
    private String result = null;
    private Boolean loadUrl = false;
    private boolean check;

    private int maxResponseTime;

    private String cred;

//    GetCredUrl getCredUrl = (GetCredUrl) new GetCredUrl().execute();;

//    GetCredUrl getCredUrl;
//    getCredUrl =  new GetCredUrl();
//    getCredUrl.execute();


    @Override
    public Integer getAsInteger(String name) {
        if (name.equals("maxInputLength")) {
            int maxInputLength = 7;
            return maxInputLength;
        }
        if (name.equals("maxResponseTime")) {
            maxResponseTime = 5;
            return maxResponseTime;
        }
        return null;
    }

    @Override
    public void update(String name, Integer value) {

    }

    @Override
    public Double getAsDouble(String name) {
        return null;
    }

    @Override
    public void update(String name, Double value) {

    }

    @Override
    public Float getAsFloat(String name) {
        return null;
    }

    @Override
    public void update(String name, Float value) {

    }

    @Override
    public Long getAsLong(String name) {
        return null;
    }

    @Override
    public void update(String name, Long value) {

    }

    @Override
    public String get(String name) {
        if (name.equals("getCredentials")) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(maxResponseTime, TimeUnit.SECONDS)
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(credUrl)
                    .build();

            Call call = client.newCall(request);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("response code "+response.code());
                    if (!response.isSuccessful()) {
                        System.out.println("error : response code = " + response.code());
                        throw new IOException("Unexpected code " + response);
                    } else {
                        System.out.println(response.receivedResponseAtMillis()-response.sentRequestAtMillis());    //404:false
//                        System.out.println("isCanceled "+call.isCanceled());
                        cred = response.body().string();
                        System.out.println("cred\n" + cred);
//                        System.out.println("isCanceled_o "+call.isCanceled());
                    }
//                    System.out.println("isCanceled_o "+call.isCanceled());
                    countDownLatch.countDown();
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("isCanceled(failure) "+call.isCanceled());
                    System.out.println("isExecuted(failure) "+call.isExecuted());
                    e.printStackTrace();
//                    System.out.println("Timeout");
                    countDownLatch.countDown();

                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cred;
//            if (call.isCanceled() == false){
//                return cred;
//            }
//            else {
//                System.out.println("ERROR getting credential");
//                return null;
//            }

//            while (cred == null) {
////                System.out.println("getting credential");
//                if (cred != null) break;
//            }

//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//////            GetCredUrl x = new GetCredUrl().execute();
//////            String old = null;
////            long start = System.currentTimeMillis();
////            System.out.println(start);
////
//////            while (System.currentTimeMillis() < start+(maxResponseTime*1000)) {
////            while (!check && System.currentTimeMillis() < start+(maxResponseTime*1000)) {
//////            while (result.isEmpty()) {
//////                System.out.println(System.currentTimeMillis());
//////                if (check == true) {
////                    try {
////                        Thread.sleep(1000);
////                        System.out.println("getting credential");
//////                    System.out.println(s);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                        break;
////                    }
//////                } else System.out.println("ERROR");
////            }
//            System.out.println("result getCredentials " + cred);
//            return cred;

//            try {
//                Thread.sleep(5000);
//                System.out.println("result getCredentials " + result);
//                return result;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        }
        if (name.equals("languageCode")) {
            String language;

            language = "en";
//            language = "th";
            return language;
        }
        return null;
    }

    @Override
    public void update(String name, String value) {

    }

    @Override
    public Boolean getAsBoolean(String name) {
        return null;
    }

    @Override
    public void update(String name, Boolean value) {

    }

    @Override
    public List<String> getAsList(String name) {
        return null;
    }

    @Override
    public void update(String name, List<String> value) {

    }

    @Override
    public String dump() {
        return "dump check";
    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {
//        System.out.println("result ex " + result);
//        new GetCredUrl().execute();
    }

    private String getCredHttp() {
//        final String[] cred = {String.valueOf(new String[1])};

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(credUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {   //suscessful return
                if (!response.isSuccessful()) {
                    System.out.println("error : response code = " + response.code());
                    throw new IOException("Unexpected code " + response);
                } else {
                    System.out.println("code "+response.code());
                    System.out.println("response "+response.isSuccessful());    //404:false
                    String credx = response.body().string();
                    System.out.println("cred " + credx);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {       //conectivity problem, timeout
                e.printStackTrace();
                fail();
            }
        });
        return null;
    }

    class GetCredUrl extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {       //Boolean / Void
            URL url;
            try {
//                System.out.println("result test " + result);
//                System.out.println(check);
                url = new URL(credUrl);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    string = String.format("%s%s", string, stringBuffer);
//                    check = true;
                }
                bufferedReader.close();
                result = string;
                check = true;
                System.out.println("result GetCredUrl " + result);

//                InputStream test = new InputStreamReader(url.openStream());
//                credentials = GoogleCredentials.fromStream(test);
            } catch (IOException e){
                e.printStackTrace();
//                result = e.toString();
                check = false;
                System.out.println("result ex " + result);
            }

            return null;
        }
    }

//    private void retrofit {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(1, TimeUnit.MINUTES)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(credUrl)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        System.out.println(retrofit);
//    }

    private String type;
    private String project_id;
    private String client_id;

    public String getType() {
        return type;
    }
    public void setTyoe (String type) {
        this.type = type;
    }
    public String getProjectId() {
        return project_id;
    }

    public String getClientId() {
        return client_id;
    }


//    public void setProjectId (String project_id) {
//        this.project_id = project_id;
//    }

//    public MyConfig (String typeName)

}
