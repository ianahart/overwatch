package com.hart.overwatch.reaction.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReactionRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @Size(min = 1, max = 50, message = "Emoji must be between 1 and 50 characters")
    private String emoji;

    public CreateReactionRequest() {

    }

    public CreateReactionRequest(Long userId, String emoji) {
        this.userId = userId;
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public Long getUserId() {
        return userId;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
