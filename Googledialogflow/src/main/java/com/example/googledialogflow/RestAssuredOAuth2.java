//package com.example.googledialogflow;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import io.restassured.RestAssured;
//import static io.restassured.RestAssured.*;
//
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Base64;
//public class RestAssuredOAuth2 {
//    public static String clientId = "452431082519-93hbf6craprqs8h8kppemmbhfndl0948.apps.googleusercontent.com";
//    public static String redirectUri = "https://www.getpostman.com/oauth2/callback";
//    public static String scope = "https://www.googleapis.com/auth/dialogflow";
//    public static String username = "justlikeheaven.chan@gmail.com";
//    public static String password = "Just_like_heaven_69";
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String encode(String str1, String str2) {
//        return new String(Base64.getEncoder().encode((str1 + ":" + str2).getBytes()));
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static Response getCode(){
//        String authorization = encode(username, password);
//        return
//                given()
//                        .header("authorization", "Basic " + authorization)
//                        .contentType(ContentType.URLENC)
//                        .formParam("response_type", "code")
//                        .queryParam("client_id", clientId)
//                        .queryParam("redirect_uri", redirectUri)
//                        .queryParam("scope", scope)
//                        .post("/oauth2/authorize")
//                        .then()
//                        .statusCode(200)
//                        .extract()
//                        .response();
//    }
//    public static String parseForOAuth2Code(Response response) {
//        return response.jsonPath().getString("code");
//    }
//    @BeforeAll
//    public static void setup() {
//        RestAssured.baseURI = "https://dialogflow.googleapis.com";
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Test
//    public void iShouldGetCode() {
//        Response response = getCode();
//        String code = parseForOAuth2Code(response);
//
//        Assertions.assertNotNull(code);
//    }
//
//}
