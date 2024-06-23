package com.hart.overwatch.notification.dto;

import java.sql.Timestamp;
import com.hart.overwatch.notification.NotificationRole;
import com.hart.overwatch.notification.NotificationType;

public class NotificationDto {
    private Long id;

    private Timestamp createdAt;

    private String text;

    private Long receiverId;

    private Long senderId;

    private String avatarUrl;

    private NotificationType notificationType;

    private NotificationRole notificationRole;


    public NotificationDto() {

    }

    public NotificationDto(Long id, Timestamp createdAt, String text, Long receiverId,
            Long senderId, String avatarUrl, NotificationType notificationType,
            NotificationRole notificationRole) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.avatarUrl = avatarUrl;
        this.notificationType = notificationType;
        this.notificationRole = notificationRole;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public NotificationRole getNotificationRole() {
        return notificationRole;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setNotificationRole(NotificationRole notificationRole) {
        this.notificationRole = notificationRole;
    }
}
