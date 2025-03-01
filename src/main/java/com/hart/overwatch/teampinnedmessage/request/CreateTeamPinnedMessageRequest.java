package com.hart.overwatch.teampinnedmessage.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTeamPinnedMessageRequest {

    @Size(min = 1, max = 100, message = "Message must not exceed 100 characters")
    private String message;

    @NotNull(message = "User Id cannot be null")
    private Long userId;

    public CreateTeamPinnedMessageRequest() {

    }

    public CreateTeamPinnedMessageRequest(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
