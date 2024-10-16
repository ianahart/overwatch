package com.hart.overwatch.blockuser.response;

public class CreateBlockUserResponse {

    private String message;

    public CreateBlockUserResponse() {

    }

    public CreateBlockUserResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
