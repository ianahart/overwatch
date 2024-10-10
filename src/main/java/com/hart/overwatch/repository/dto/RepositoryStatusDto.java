package com.hart.overwatch.repository.dto;

import com.hart.overwatch.repository.RepositoryStatus;

public class RepositoryStatusDto {

    private RepositoryStatus status;

    public RepositoryStatusDto() {

    }

    public RepositoryStatusDto(RepositoryStatus status) {
        this.status = status;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }
}
