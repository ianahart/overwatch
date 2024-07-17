package com.hart.overwatch.repository.dto;

import java.util.List;
import com.hart.overwatch.github.dto.GitHubTreeNodeDto;

public class RepositoryContentsDto {

    private FullRepositoryDto repository;

    private List<GitHubTreeNodeDto> tree;


    public RepositoryContentsDto() {

    }

    public RepositoryContentsDto(FullRepositoryDto repository, List<GitHubTreeNodeDto> tree) {
        this.repository = repository;
        this.tree = tree;
    }

    public FullRepositoryDto getRepository() {
        return repository;
    }

    public List<GitHubTreeNodeDto> getTree() {
        return tree;
    }

    public void setRepository(FullRepositoryDto repository) {
        this.repository = repository;
    }

    public void setTree(List<GitHubTreeNodeDto> tree) {
        this.tree = tree;
    }
}
