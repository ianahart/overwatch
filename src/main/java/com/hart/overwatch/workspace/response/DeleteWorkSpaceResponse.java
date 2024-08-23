package com.hart.overwatch.workspace.response;

public class DeleteWorkSpaceResponse {

    private String message;

    public DeleteWorkSpaceResponse() {

    }

    public DeleteWorkSpaceResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
