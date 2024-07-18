package com.hart.overwatch.github.dto;

import java.util.List;

public class GitHubRepositoryFileDto {

    private String content;

    private List<String> languages;

    public GitHubRepositoryFileDto() {

    }

    public GitHubRepositoryFileDto(String content, List<String> languages) {
        this.content = content;
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
