package com.hart.overwatch.repository.request;

public class CreateRepositoryFileRequest {
    private String owner;

    private String repoName;

    private String path;

    private Long githubId;

    public CreateRepositoryFileRequest() {

    }

    public CreateRepositoryFileRequest(String owner, String repoName, String path, Long githubId) {
        this.owner = owner;
        this.repoName = repoName;
        this.path = path;
        this.githubId = githubId;
    }

    public String getPath() {
        return path;
    }

    public Long getGithubId() {
        return githubId;
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

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }
}
