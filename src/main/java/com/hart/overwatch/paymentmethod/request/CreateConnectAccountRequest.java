package com.hart.overwatch.paymentmethod.request;

public class CreateConnectAccountRequest {

    private String email;

    public CreateConnectAccountRequest() {

    }

    public CreateConnectAccountRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
