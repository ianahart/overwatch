package com.hart.overwatch.feedbacktemplate.response;

import com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto;

public class GetFeedbackTemplateResponse {

    private String message;

    private FeedbackTemplateDto data;

    public GetFeedbackTemplateResponse() {

    }

    public GetFeedbackTemplateResponse(String message, FeedbackTemplateDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackTemplateDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(FeedbackTemplateDto data) {
        this.data = data;
    }
}
