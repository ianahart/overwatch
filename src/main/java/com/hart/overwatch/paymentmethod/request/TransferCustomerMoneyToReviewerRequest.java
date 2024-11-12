package com.hart.overwatch.paymentmethod.request;

import jakarta.validation.constraints.NotNull;

public class TransferCustomerMoneyToReviewerRequest {

    @NotNull
    private Long repositoryId;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long reviewerId;

    public TransferCustomerMoneyToReviewerRequest() {

    }

    public TransferCustomerMoneyToReviewerRequest(Long repositoryId, Long ownerId,
            Long reviewerId) {
        this.repositoryId = repositoryId;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }
}
