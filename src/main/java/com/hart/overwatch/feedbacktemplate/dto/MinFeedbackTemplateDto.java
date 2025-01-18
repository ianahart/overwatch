package com.hart.overwatch.feedbacktemplate.dto;

public class MinFeedbackTemplateDto {

    private Long id;

    private Long userId;

    public MinFeedbackTemplateDto() {

    }

    public MinFeedbackTemplateDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
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

}
