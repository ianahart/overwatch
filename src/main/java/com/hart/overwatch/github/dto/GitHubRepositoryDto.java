package com.hart.overwatch.github.dto;

public class GitHubRepositoryDto {

    private Long id;

    private String fullName;

    private String avatarUrl;

    private String htmlUrl;

    private String language;

    private Integer stargazersCount;


    public GitHubRepositoryDto() {

    }

    public GitHubRepositoryDto(Long id, String fullName, String avatarUrl, String htmlUrl,
            String language, Integer stargazersCount) {
        this.id = id;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.language = language;
        this.stargazersCount = stargazersCount;

    }


    public Long getId() {
        return id;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLanguage() {
        return language;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Integer getStargazersCount() {
        return stargazersCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }
}
