package com.hart.overwatch.topic.response;

public class CreateTopicResponse {

    private String message;

    public CreateTopicResponse() {

    }

    public CreateTopicResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
