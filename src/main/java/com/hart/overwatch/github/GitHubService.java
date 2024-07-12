package com.hart.overwatch.github;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GitHubService {

    private final OkHttpClient okHttpClient;

    @Value("${GITHUB_CLIENT_ID}")
    private String clientId;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.github.token-uri}")
    private String tokenUri;

    @Autowired
    public GitHubService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getAccessToken(String code) {
        String json = String.format(
                "{\"client_id\":\"%s\", \"client_secret\":\"%s\", \"code\":\"%s\", \"redirect_uri\":\"%s\"}",
                clientId, clientSecret, code, redirectUri);

        RequestBody body =
                RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder().url(tokenUri).header("Accept", "application/json")
                .post(body).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.getString("access_token");
            } else {
                throw new RuntimeException("Failed to get access token: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

}

