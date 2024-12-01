package com.hart.overwatch.teamcomment.dto;

public class MinTeamCommentDto {

    private String content;

    private String tag;

    public MinTeamCommentDto() {

    }

    public MinTeamCommentDto(String content, String tag) {
        this.content = content;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
