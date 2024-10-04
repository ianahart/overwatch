package com.hart.overwatch.repository.dto;

import com.hart.overwatch.github.dto.GitHubTreeDto;

public class RepositoryContentsDto {

    private FullRepositoryDto repository;

    private GitHubTreeDto contents;


    public RepositoryContentsDto() {

    }

    public RepositoryContentsDto(FullRepositoryDto repository, GitHubTreeDto contents) {
        this.repository = repository;
        this.contents = contents;
    }

    public FullRepositoryDto getRepository() {
        return repository;
    }

    public GitHubTreeDto getContents() {
        return contents;
    }

    public void setRepository(FullRepositoryDto repository) {
        this.repository = repository;
    }

    public void setContents(GitHubTreeDto contents) {
        this.contents = contents;
    }
}
