package com.hart.overwatch.feedbacktemplate.response;

public class CreateFeedbackTemplateResponse {

    private String message;

    public CreateFeedbackTemplateResponse() {

    }

    public CreateFeedbackTemplateResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
