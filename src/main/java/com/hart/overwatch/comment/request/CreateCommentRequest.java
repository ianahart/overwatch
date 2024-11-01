package com.hart.overwatch.comment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

    @NotNull(message = "Please provide a userId")
    private Long userId;

    @NotNull(message = "Please provide a topicId")
    private Long topicId;

    @Size(min = 1, max = 400, message = "A comment must be between 1 and 400 characters")
    private String content;

    public CreateCommentRequest() {

    }

    public CreateCommentRequest(Long userId, Long topicId, String content) {
        this.userId = userId;
        this.topicId = topicId;
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
