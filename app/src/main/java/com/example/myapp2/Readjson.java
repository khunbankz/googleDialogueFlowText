package com.example.myapp2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
public class Readjson {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("C:\\Users\\justl\\AndroidStudioProjects\\MyApp2\\app\\src\\main\\res\\raw\\application_default_credentials.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String name = (String) jsonObject.get("project_id");
            System.out.println(name);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
