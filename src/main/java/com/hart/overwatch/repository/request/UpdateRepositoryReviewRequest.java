package com.hart.overwatch.repository.request;

import com.hart.overwatch.repository.RepositoryStatus;

public class UpdateRepositoryReviewRequest {

    private RepositoryStatus status;

    private String feedback;


    public UpdateRepositoryReviewRequest() {

    }

    public UpdateRepositoryReviewRequest(RepositoryStatus status, String feedback) {
        this.status = status;
        this.feedback = feedback;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
