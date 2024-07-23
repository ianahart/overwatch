package com.hart.overwatch.repository.response;

import com.hart.overwatch.repository.dto.RepositoryReviewDto;

public class UpdateRepositoryReviewResponse {

    private String message;

    private RepositoryReviewDto data;

    public UpdateRepositoryReviewResponse() {

    }

    public UpdateRepositoryReviewResponse(String message, RepositoryReviewDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public RepositoryReviewDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(RepositoryReviewDto data) {
        this.data = data;
    }
}
