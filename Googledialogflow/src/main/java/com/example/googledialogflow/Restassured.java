package com.example.googledialogflow;
//import io.restassured.RestAssured;
//import io.restassured.authentication.CertificateAuthSettings;
//import io.restassured.authentication.PreemptiveBasicAuthScheme;
//import io.restassured.config.RestAssuredConfig;
//import io.restassured.config.SSLConfig;
//import io.restassured.path.json.JsonPath;
//import io.restassured.response.Response;
//
//import io.restassured.http.ContentType;
//import io.restassured.specification.RequestSpecification;
//
//import static io.restassured.RestAssured.*;
//import static io.restassured.config.RestAssuredConfig.newConfig;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.X509HostnameVerifier;


import com.google.auth.Credentials;
import com.google.auth.appengine.AppEngineCredentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

public class Restassured {
    public static void main(String[] args) throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {




//        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\justl\\AppData\\Roaming\\gcloud\\application_default_credentials.json"));
//        credentials.refreshIfExpired();
//        AccessToken token = credentials.getAccessToken();
//        System.out.println(token);


        //RestAssured.config() = RestAssuredConfig.newConfig().sslConfig(new SSLConfig());
        //X509HostnameVerifier hostnameVerifier = new NopX509HostnameVerifierApache();
//        String accessTokenResponse = given()
//                //.header("Authorization","Bearer"+"ya29.a0ARrdaM_vZyGjpJYVAsMQHwKVUQoBoEHeR2RHG0dtxrfPTZIC3sB3R46lv9U5p7eMCnvuRZnkjXX2z8PJ86c170D3ZJgdecwTtfxT-1aEXaEUQw0GujQ3ws9hrv27FnpcxZWLK6bhk4yTgfMmg5NO33vGBIu3wg")
//                //.config(newConfig().sslConfig(new SSLConfig("/truststore_javanet.jks", "test1234"))
////                .queryParam("code","")
////                .queryParam("client_id","452431082519-93hbf6craprqs8h8kppemmbhfndl0948.apps.googleusercontent.com")
////                .queryParam("client_secret","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")
////                .queryParam("redirect_uri","https://oauth-redirect.googleusercontent.com/r/submarine-chatbot-dsuq")
////                .queryParam("grant_type","authorization_code")
//                .body("{\n" +
//                        "  \"query_input\": {\n" +
//                        "    \"text\": {\n" +
//                        "      \"text\": \"สวัสดี\",\n" +
//                        "      \"language_code\": \"en-US\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}")
//                .when().log().all()
//                .post("https://accounts.google.com/o/oauth2/token").asString();
//        JsonPath js = new JsonPath(accessTokenResponse);
//        String accessToken = js.getString("access_token");
//
//        String response = given().queryParam("access_token","")
//                .when().log().all()
//                .get("https://dialogflow.googleapis.com/v2/projects/submarine-chatbot-dsuq/agent/intents").asString();
//        System.out.println(response);
//////////////////////////////////////////////////////////////////////
        GoogleCredentials googleCred = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\justl\\AppData\\Roaming\\gcloud\\application_default_credentials.json"));
        GoogleCredentials scoped = googleCred.createScoped("https://www.googleapis.com/auth/dialogflow");
        String token = scoped.refreshAccessToken().toString();
        String cred = googleCred.getAuthenticationType();
        //System.out.println(cred);
        System.out.println(token);
        //const sessionCli = new dialogflow
//        given().auth().oauth2(token).when().get("https://dialogflow.googleapis.com/v2/projects/submarine-chatbot-dsuq/agent/intents")
//                .then().statusCode(200);

//        SSLConfig sslConfig = config().getSSLConfig();
//        RestAssured.certificate("https://www.googleapis.com/oauth2/v1/certs", "notasecret", CertificateAuthSettings.certAuthSettings().keyStoreType(sslConfig.getKeyStoreType()).trustStore(sslConfig.getTrustStore()).keyStore(sslConfig.getKeyStore()).trustStore(sslConfig.getTrustStore()).x509HostnameVerifier(sslConfig.getX509HostnameVerifier()));
//        RestAssured.given().auth().oauth2(token).when().get("https://dialogflow.googleapis.com/v2/projects/submarine-chatbot-dsuq/agent/intents")
//                .then().statusCode(200);
//
        KeyStore keyStore = null;
        //SSLConfig config = null;
        String password ="notasecret";
        try{
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(
                    new FileInputStream("C:\\Users\\justl\\Downloads\\submarine-chatbot-dsuq-8b78290d4369.p12"),
                    password.toCharArray());

        } catch (Exception e) {
            System.out.println("Error while loading keystore>>>>>>>>>>");
            e.printStackTrace();
        }

        //RestAssured.config = RestAssured.config().sslConfig(config);


//        RestAssured.baseURI = "https://dialogflow.googleapis.com/";
//        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
//        authScheme.setUserName("Justlikeheaven.chan@gmail.com");
//        authScheme.setPassword("Just_like_heaven_69");
//        RestAssured.authentication = authScheme;



//                String response = given().queryParam("access_token",token)
//                        .queryParam("")
//                .when().log().all()
//                .get("https://dialogflow.googleapis.com/v2/projects/submarine-chatbot-dsuq/agent/intents").asString();
//        System.out.println(response);



        //System.out.println(scoped.refreshAccessToken().toString());
//        String url = "https://oauth2.googleapis.com/token";
//        RequestSpecification http_req = RestAssured.given()
//                .queryParam("grant_type","client_credentials")
//                .queryParam("client_id","102658935437626514324102658935437626514324102658935437626514324")
//                .queryParam("client_secret","9b1b40bf5cbce8bbb930684c620d33603a580fe1")
//                .header("Content-Type","application/json");
//        Response response = http_req.post(url);
//        int status_code = response.getStatusCode();
//        String response_body = response.asString();
//        System.out.println("---- status code -----"+status_code);
//        System.out.println("----respomse body----"+response_body);













        //////////////////////////////////////////////////////////////////////////////////
        //RestAssured.baseURI = "https://dialogflow.googleapis.com/v2";
//        given().formParam("client_id","452431082519-93hbf6craprqs8h8kppemmbhfndl0948.apps.googleusercontent.com")
//                .formParam("client_secret","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")
//                .formParam("grant_type","client_credentials")
//                .header("Content-Type","application/json")
//                .body("{\n" +
//                        "  \"query_input\": {\n" +
//                        "    \"text\": {\n" +
//                        "      \"text\": \"I know french\",\n" +
//                        "      \"language_code\": \"en-US\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}")
//                .post("https://accounts.google.com/o/oauth2/token");

        //String token = resp.jsonPath(),get("access_token");
//                given().formParam("client_id","452431082519-93hbf6craprqs8h8kppemmbhfndl0948.apps.googleusercontent.com")
//                .formParam("client_secret","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")
//                .formParam("grant_type","client_credentials")
//                .post("https://accounts.google.com/o/oauth2/token");
        //String token = resp.jsonPath(),ger()'';
        //given().auth().oauth2("").post();
        /////////////////////////////////////////////////////



    }
//    public void verify(String host, X509Certificate cert){
//        if (allowe)
//
//    }

}
