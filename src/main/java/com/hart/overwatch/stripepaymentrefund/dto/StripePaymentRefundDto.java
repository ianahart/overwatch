package com.hart.overwatch.stripepaymentrefund.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.stripepaymentrefund.PaymentRefundStatus;

public class StripePaymentRefundDto {

    private Long id;

    private String adminNotes;

    private Long amount;

    private String currency;

    private String reason;

    private PaymentRefundStatus status;

    private Long stripePaymentIntentId;

    private LocalDateTime createdAt;

    private Long userId;

    private String fullName;

    public StripePaymentRefundDto() {

    }

    public StripePaymentRefundDto(Long id, String adminNotes, Long amount, String currency,
            String reason, PaymentRefundStatus status, Long stripePaymentIntentId,
            LocalDateTime createdAt, Long userId, String fullName) {
        this.id = id;
        this.adminNotes = adminNotes;
        this.amount = amount;
        this.currency = currency;
        this.reason = reason;
        this.status = status;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.createdAt = createdAt;
        this.userId = userId;
        this.fullName = fullName;
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

    public String getReason() {
        return reason;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PaymentRefundStatus getStatus() {
        return status;
    }

    public Long getStripePaymentIntentId() {
        return stripePaymentIntentId;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(PaymentRefundStatus status) {
        this.status = status;
    }

    public void setStripePaymentIntentId(Long stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
}


