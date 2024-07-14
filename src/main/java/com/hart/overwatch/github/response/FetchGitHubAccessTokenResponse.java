package com.hart.overwatch.github.response;


public class FetchGitHubAccessTokenResponse {

    private String message;

    private String accessToken;


    public FetchGitHubAccessTokenResponse() {

    }

    public FetchGitHubAccessTokenResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


}
