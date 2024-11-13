package com.hart.overwatch.stripepaymentrefund.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateStripePaymentRefundRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long stripePaymentIntentId;

    @Size(min = 1, max = 1000, message = "Your reason must be between 1 and 1000 characters")
    private String reason;

    public CreateStripePaymentRefundRequest() {

    }

    public CreateStripePaymentRefundRequest(Long userId, Long stripePaymentIntentId,
            String reason) {
        this.userId = userId;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStripePaymentIntentId(Long stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
