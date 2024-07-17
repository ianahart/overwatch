package com.hart.overwatch.repository.request;

public class CreateRepositoryFileRequest {
    private String owner;

    private String repoName;

    private String path;

    private String accessToken;

    public CreateRepositoryFileRequest() {

    }

    public CreateRepositoryFileRequest(String owner, String repoName, String path,
            String accessToken) {
        this.owner = owner;
        this.repoName = repoName;
        this.path = path;
        this.accessToken = accessToken;
    }

    public String getPath() {
        return path;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
