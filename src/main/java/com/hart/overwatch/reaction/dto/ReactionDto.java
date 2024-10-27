package com.hart.overwatch.reaction.dto;

public class ReactionDto {

    private String emoji;

    private Integer count;

    public ReactionDto() {

    }

    public ReactionDto(String emoji, Integer count) {
        this.emoji = emoji;
        this.count = count;
    }

    public String getEmoji() {
        return emoji;
    }

    public Integer getCount() {
        return count;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
