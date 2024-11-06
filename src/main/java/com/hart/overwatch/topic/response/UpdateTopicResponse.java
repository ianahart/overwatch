package com.hart.overwatch.topic.response;

public class UpdateTopicResponse {

    private String message;

    public UpdateTopicResponse() {

    }

    public UpdateTopicResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
