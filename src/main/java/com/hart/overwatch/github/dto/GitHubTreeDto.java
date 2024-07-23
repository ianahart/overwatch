package com.hart.overwatch.github.dto;

import java.util.List;

public class GitHubTreeDto {

    private List<String> languages;

    private List<GitHubTreeNodeDto> tree;


    public GitHubTreeDto() {

    }

    public GitHubTreeDto(List<String> languages, List<GitHubTreeNodeDto> tree) {
        this.languages = languages;
        this.tree = tree;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<GitHubTreeNodeDto> getTree() {
        return tree;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setTree(List<GitHubTreeNodeDto> tree) {
        this.tree = tree;
    }
}
