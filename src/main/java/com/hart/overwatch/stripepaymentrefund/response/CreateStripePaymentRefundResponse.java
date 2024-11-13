package com.hart.overwatch.stripepaymentrefund.response;

public class CreateStripePaymentRefundResponse {

    private String message;

    public CreateStripePaymentRefundResponse() {

    }

    public CreateStripePaymentRefundResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
