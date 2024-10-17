package com.hart.overwatch.blockuser.response;

public class DeleteBlockUserResponse {

    private String message;

    public DeleteBlockUserResponse() {

    }

    public DeleteBlockUserResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
