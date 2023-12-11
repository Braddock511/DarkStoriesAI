package org.test.darkstoriesai;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {
    private final OkHttpClient client;
    private final String url = "0.0.0.0:5110"; // If it doesn't work, enter your local IP address
    public API() {
        int timeoutSeconds = 60;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build();
    }

    public JSONObject createStoryRequest(String story_format, String sessionId) {
        try {
            // Define the request form data
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("story_format", story_format);
            jsonBody.put("session_id", sessionId);

            // Serialize the JSON object to a string
            String json = jsonBody.toString();
            RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(String.format("http://%s/story", url))
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            return new JSONObject(jsonResponse);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject ask(String prompt, String sessionId) {
        try {
            // Define the request form data
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_prompt", prompt);
            jsonBody.put("session_id", sessionId);

            // Serialize the JSON object to a string
            String json = jsonBody.toString();
            RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(String.format("http://%s/question", url))
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            String jsonResponse = response.body().string();
            return new JSONObject(jsonResponse);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject sendSolution(String prompt, String sessionId) {
        try {
            // Define the request form data
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_prompt", prompt);
            jsonBody.put("session_id", sessionId);

            // Serialize the JSON object to a string
            String json = jsonBody.toString();
            RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(String.format("http://%s/solve", url))
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            return new JSONObject(jsonResponse);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> storiesRequest() {
        try {
            Request request = new Request.Builder()
                    .url(String.format("http://%s/stories", url))
                    .header("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // Use Gson to parse the JSON string
            Gson gson = new Gson();
            Type storyListType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            return gson.fromJson(responseBody, storyListType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
