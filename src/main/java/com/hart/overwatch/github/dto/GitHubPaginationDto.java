package com.hart.overwatch.github.dto;

import java.util.List;

public class GitHubPaginationDto {

    private String nextPageUrl;

    private List<GitHubRepositoryDto> repositories;


    public GitHubPaginationDto() {

    }

    public GitHubPaginationDto(String nextPageUrl, List<GitHubRepositoryDto> repositories) {
        this.nextPageUrl = nextPageUrl;
        this.repositories = repositories;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public List<GitHubRepositoryDto> getRepositories() {
        return repositories;
    }


    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public void setRepositories(List<GitHubRepositoryDto> repositories) {
        this.repositories = repositories;
    }

}
