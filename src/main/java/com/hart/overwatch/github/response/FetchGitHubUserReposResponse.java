package com.hart.overwatch.github.response;

import com.hart.overwatch.github.dto.GitHubPaginationDto;

public class FetchGitHubUserReposResponse {

    private String message;

    private GitHubPaginationDto data;


    public FetchGitHubUserReposResponse() {

    }

    public FetchGitHubUserReposResponse(String message, GitHubPaginationDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public GitHubPaginationDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(GitHubPaginationDto data) {
        this.data = data;
    }
}
