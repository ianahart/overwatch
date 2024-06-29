package com.hart.overwatch.chatmessage.request;

public class CreateChatMessageRequest {

    private Long connectionId;

    private Long userId;

    private String text;


    public CreateChatMessageRequest() {

    }

    public CreateChatMessageRequest(Long connectionId, Long userId, String text) {
        this.connectionId = connectionId;
        this.userId = userId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}
