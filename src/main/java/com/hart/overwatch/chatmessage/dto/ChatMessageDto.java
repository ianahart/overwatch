package com.hart.overwatch.chatmessage.dto;

import java.sql.Timestamp;


public class ChatMessageDto {

    private Long id;

    private String firstName;

    private String lastName;

    private Timestamp createdAt;

    private String text;

    private String avatarUrl;

    private Long connectionId;

    private Long userId;

    public ChatMessageDto() {

    }

    public ChatMessageDto(Long id, String firstName, String lastName, Timestamp createdAt,
            String text, String avatarUrl, Long connectionId, Long userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.text = text;
        this.avatarUrl = avatarUrl;
        this.connectionId = connectionId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}
