package com.hart.overwatch.paymentmethod.response;


public class CreateUserPaymentMethodResponse {

    private String message;

    public CreateUserPaymentMethodResponse() {

    }

    public CreateUserPaymentMethodResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
