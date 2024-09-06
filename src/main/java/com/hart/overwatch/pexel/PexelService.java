package com.hart.overwatch.pexel;

import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PexelService {

    @Value("${PEXELS_API_KEY}")
    private String PEXELS_API_KEY;

    private final OkHttpClient okHttpClient;


    @Autowired
    public PexelService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    public List<String> fetchPexelPhotos(String q) throws IOException {
        String query = q.trim().length() > 0 ? q : "sky";
        String url = String.format("https://api.pexels.com/v1/search?query=%s", query);

        Request request =
                new Request.Builder().url(url).header("Authorization", PEXELS_API_KEY).build();

        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Pexels API request failed: " + response);
        }

        String responseBody = response.body().string();
        return parseJsonResults(responseBody);
    }

    private List<String> parseJsonResults(String jsonResponseData) {
        List<String> photos = new ArrayList<>();
        JSONObject obj = new JSONObject(jsonResponseData);
        JSONArray arr = obj.getJSONArray("photos");

        for (int i = 0; i < arr.length(); i++) {
            String photo = arr.getJSONObject(i).getJSONObject("src").get("medium").toString();
            photos.add(photo);
        }
        return photos;
    }
}


