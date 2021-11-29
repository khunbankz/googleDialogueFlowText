package com.example.googledialogflow;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
public class AccessToken {
    public static void main(String[] args) throws IOException {
        System.out.println("Access Token:"+getAccessToken());
    }
    public static String getAccessToken() throws IOException {
        return GoogleCredentials
                .fromStream(new FileInputStream("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\main\\res\\raw\\application_default_credentials.json"))
                .createScoped("https://www.googleapis.com/auth/cloud-platform")
                .refreshAccessToken()
                .getTokenValue();
    }
}
