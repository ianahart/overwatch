package com.hart.overwatch.repository.dto;

import com.hart.overwatch.repository.RepositoryStatus;

public class RepositoryReviewDto {

    private RepositoryStatus status;

    private String feedback;

    public RepositoryReviewDto() {

    }

    public RepositoryReviewDto(RepositoryStatus status, String feedback) {
        this.status = status;
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }


}
