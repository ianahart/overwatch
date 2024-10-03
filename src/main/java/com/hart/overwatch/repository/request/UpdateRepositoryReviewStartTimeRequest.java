package com.hart.overwatch.repository.request;

import java.time.LocalDateTime;
import com.hart.overwatch.repository.RepositoryStatus;

public class UpdateRepositoryReviewStartTimeRequest {

    private LocalDateTime reviewStartTime;

    private RepositoryStatus status;

    public UpdateRepositoryReviewStartTimeRequest() {

    }

    public UpdateRepositoryReviewStartTimeRequest(LocalDateTime reviewStartTime,
            RepositoryStatus status) {
        this.reviewStartTime = reviewStartTime;
        this.status = status;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public LocalDateTime getReviewStartTime() {
        return reviewStartTime;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }

    public void setReviewStartTime(LocalDateTime reviewStartTime) {
        this.reviewStartTime = reviewStartTime;
    }
}
