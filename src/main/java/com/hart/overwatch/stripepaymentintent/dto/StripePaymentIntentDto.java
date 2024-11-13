package com.hart.overwatch.stripepaymentintent.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;

public class StripePaymentIntentDto {

    private Long id;

    private Long amount;

    private String currency;

    private String fullName;

    private Long reviewerId;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private PaymentIntentStatus status;

    public StripePaymentIntentDto() {

    }

    public StripePaymentIntentDto(Long id, Long amount, String currency, String fullName,
            Long reviewerId, String avatarUrl, LocalDateTime createdAt,
            PaymentIntentStatus status) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.fullName = fullName;
        this.reviewerId = reviewerId;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(PaymentIntentStatus status) {
        this.status = status;
    }
}
