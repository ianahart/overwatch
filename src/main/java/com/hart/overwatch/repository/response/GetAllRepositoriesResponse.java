package com.hart.overwatch.repository.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.repository.dto.RepositoryDto;

public class GetAllRepositoriesResponse {

    private String message;

    private PaginationDto<RepositoryDto> data;



    public GetAllRepositoriesResponse() {

    }

    public GetAllRepositoriesResponse(String message, PaginationDto<RepositoryDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<RepositoryDto> getData() {
        return data;
    }

    public void setData(PaginationDto<RepositoryDto> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
