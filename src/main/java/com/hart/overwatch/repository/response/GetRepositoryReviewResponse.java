package com.hart.overwatch.repository.response;

import com.hart.overwatch.repository.dto.RepositoryContentsDto;

public class GetRepositoryReviewResponse {

    private String message;

    private RepositoryContentsDto data;

    public GetRepositoryReviewResponse() {

    }

    public GetRepositoryReviewResponse(String message, RepositoryContentsDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public RepositoryContentsDto getData() {
        return data;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(RepositoryContentsDto data) {
        this.data = data;
    }

}
