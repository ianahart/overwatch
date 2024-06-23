package com.hart.overwatch.notification.request;

import com.hart.overwatch.notification.NotificationType;
import jakarta.validation.constraints.NotNull;

public class CreateNotificationRequest {

    @NotNull
    private Long receiverId;

    @NotNull
    private Long senderId;

    @NotNull
    private NotificationType notificationType;

    public CreateNotificationRequest() {

    }

    public CreateNotificationRequest(Long receiverId, Long senderId,
            NotificationType notificationType) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.notificationType = notificationType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }


    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
