package controller;

import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class ResponseFormer {
    public static String formErrorResponse(String message) {

        Map<String, Object> response = new HashMap<>();

        response.put("error", message);

        GsonBuilder gsonBuilder = new GsonBuilder();
        String json = gsonBuilder.create().toJson(response);


        return json;
    }
}
