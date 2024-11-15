package com.hart.overwatch.stripepaymentrefund.response;

public class UpdateStripePaymentRefundResponse {

    private String message;

    public UpdateStripePaymentRefundResponse() {

    }

    public UpdateStripePaymentRefundResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
