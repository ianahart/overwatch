package com.hart.overwatch.github.dto;

public class GitHubTreeNodeDto {

    private String path;

    private String type;

    private String sha;

    private Integer size;

    private String url;


    public GitHubTreeNodeDto() {

    }

    public GitHubTreeNodeDto(String path, String type, String sha, Integer size, String url) {
        this.path = path;
        this.type = type;
        this.sha = sha;
        this.size = size;
        this.url = url;
    }

    public String getSha() {
        return sha;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
