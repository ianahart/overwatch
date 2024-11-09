package com.hart.overwatch.paymentmethod.response;

public class CreateConnectAccountResponse {

    private String message;

    private String data;

    public CreateConnectAccountResponse() {

    }

    public CreateConnectAccountResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }
}
