package com.hart.overwatch.paymentmethod.response;

public class DeleteUserPaymentMethodResponse {

    private String message;


    public DeleteUserPaymentMethodResponse() {

    }

    public DeleteUserPaymentMethodResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
