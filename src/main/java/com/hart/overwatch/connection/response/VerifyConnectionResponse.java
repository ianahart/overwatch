package com.hart.overwatch.connection.response;

import com.hart.overwatch.connection.RequestStatus;

public class VerifyConnectionResponse {

    private String message;

    private RequestStatus data;


    public VerifyConnectionResponse() {

    }

    public VerifyConnectionResponse(String message, RequestStatus data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public RequestStatus getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(RequestStatus data) {
        this.data = data;
    }
}
