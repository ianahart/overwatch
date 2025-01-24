package com.hart.overwatch.github.response;


public class FetchGitHubAccessTokenResponse {

    private String message;

    private Long githubId;


    public FetchGitHubAccessTokenResponse() {

    }

    public FetchGitHubAccessTokenResponse(String message, Long githubId) {
        this.message = message;
        this.githubId = githubId;
    }

    public String getMessage() {
        return message;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }


}
