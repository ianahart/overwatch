package com.hart.overwatch.paymentmethod.response;

public class TransferCustomerMoneyToReviewerResponse {

    private String message;

    public TransferCustomerMoneyToReviewerResponse() {

    }

    public TransferCustomerMoneyToReviewerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
