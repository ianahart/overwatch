package com.hart.overwatch.reportcomment.response;

public class DeleteReportCommentResponse {

    private String message;

    public DeleteReportCommentResponse() {

    }

    public DeleteReportCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
