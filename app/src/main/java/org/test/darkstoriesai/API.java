package org.test.darkstoriesai;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {
    private final OkHttpClient client;
    private final String url = "0.0.0.0"; // If it doesn't work, enter your local IP address

    public API() {
        int timeoutSeconds = 3600;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build();
    }

    public String sendRequest(String prompt) {
        try {
            // Define the request form data
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_prompt", prompt);

            // Serialize the JSON object to a string
            String json = jsonBody.toString();
            RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(String.format("http://%s/api/prompt_route", url))
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.getString("Answer");
            } else {
                return "Request failed: " + response.body().string();
            }
        } catch (IOException e) {
            Log.i("API", e.toString());
            e.printStackTrace();
            return "FAILED";
        } catch (JSONException e) {
            Log.i("API", e.toString());
            throw new RuntimeException(e);
        }
    }
}
