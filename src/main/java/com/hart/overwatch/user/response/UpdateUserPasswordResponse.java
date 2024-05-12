package com.hart.overwatch.user.response;

public class UpdateUserPasswordResponse {

    private String message;


    public UpdateUserPasswordResponse() {

    }

    public UpdateUserPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
