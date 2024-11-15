package com.hart.overwatch.stripepaymentrefund.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateStripePaymentRefundRequest {

    @NotNull(message = "status must not be empty")
    private String status;

    @NotNull(message = "stripe payment intent id must not be empty")
    private Long stripePaymentIntentId;

    @Size(min = 1, max = 300, message = "Admin notes must be between 1 and 300 characters")
    private String adminNotes;

    public UpdateStripePaymentRefundRequest() {

    }

    public UpdateStripePaymentRefundRequest(Long stripePaymentIntentId, String adminNotes,
            String status) {
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.adminNotes = adminNotes;
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public String getStatus() {
        return status;
    }

    public Long getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setStripePaymentIntentId(Long stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
