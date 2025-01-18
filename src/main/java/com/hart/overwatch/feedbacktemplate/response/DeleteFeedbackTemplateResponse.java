package com.hart.overwatch.feedbacktemplate.response;

public class DeleteFeedbackTemplateResponse {

    private String message;

    public DeleteFeedbackTemplateResponse() {

    }

    public DeleteFeedbackTemplateResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
