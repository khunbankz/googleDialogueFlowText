package com.example.android_dialogflow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET("uc?id=10edZa3D8Sw-AZYP6UMfekDHMEj_q2aEz")
    Call<MyConfig> getCred();
    Call<String> getStringResponse(@Url String url);
//    MyConfig getCred (@Body MyConfig body);
}
