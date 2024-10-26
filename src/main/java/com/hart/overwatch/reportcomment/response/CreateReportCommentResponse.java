package com.hart.overwatch.reportcomment.response;

public class CreateReportCommentResponse {

    private String message;

    public CreateReportCommentResponse() {

    }

    public CreateReportCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
