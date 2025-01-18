package com.hart.overwatch.feedbacktemplate.dto;



public class FeedbackTemplateDto {

    private Long id;

    private Long userId;

    private String feedback;

    public FeedbackTemplateDto() {

    }

    public FeedbackTemplateDto(Long id, Long userId, String feedback) {
        this.id = id;
        this.userId = userId;
        this.feedback = feedback;
    }

    public Long getId() {
        return id;
    }

    public String getFeedback() {
        return feedback;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

