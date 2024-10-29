package com.hart.overwatch.notification.request;

import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hart.overwatch.notification.NotificationType;
import jakarta.validation.constraints.NotNull;

public class CreateNotificationRequest {

    @NotNull
    private Long receiverId;

    @NotNull
    private Long senderId;

    @NotNull
    private NotificationType notificationType;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Optional<String> link = Optional.empty();

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

    public Optional<String> getLink() {
        return link;
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

    public void setLink(Optional<String> link) {
        this.link = link;
    }
}
