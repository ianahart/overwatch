package com.hart.overwatch.feedbacktemplate.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateFeedbackTemplateRequest {

    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    @Size(min = 1, max = 10000)
    private String feedback;


    public CreateFeedbackTemplateRequest() {

    }

    public CreateFeedbackTemplateRequest(Long userId, String feedback) {
         this.userId = userId;
        this.feedback = feedback;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
