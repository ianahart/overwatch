package com.hart.overwatch.stripepaymentintent.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;

public class FullStripePaymentIntentDto {

    private Long id;

    private Long amount;

    private Long applicationFee;

    private LocalDateTime createdAt;

    private String currency;

    private String description;

    private PaymentIntentStatus status;

    private String userFullName;

    private String reviewerFullName;

    private String userEmail;

    private String reviewerEmail;

    private Long userId;

    private Long reviewerId;

    public FullStripePaymentIntentDto() {

    }

    public FullStripePaymentIntentDto(Long id, Long amount, Long applicationFee,
            LocalDateTime createdAt, String currency, String description,
            PaymentIntentStatus status, String userFullName, String reviewerFullName,
            String userEmail, String reviewerEmail, Long userId, Long reviewerId) {
        this.id = id;
        this.amount = amount;
        this.applicationFee = applicationFee;
        this.createdAt = createdAt;
        this.currency = currency;
        this.description = description;
        this.status = status;
        this.userFullName = userFullName;
        this.reviewerFullName = reviewerFullName;
        this.userEmail = userEmail;
        this.reviewerEmail = reviewerEmail;
        this.userId = userId;
        this.reviewerId = reviewerId;
    }

    public Long getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDescription() {
        return description;
    }

    public Long getApplicationFee() {
        return applicationFee;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getReviewerFullName() {
        return reviewerFullName;
    }

    public PaymentIntentStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(PaymentIntentStatus status) {
        this.status = status;
    }

    public void setApplicationFee(Long applicationFee) {
        this.applicationFee = applicationFee;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public void setReviewerFullName(String reviewerFullName) {
        this.reviewerFullName = reviewerFullName;
    }
}
