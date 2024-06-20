package com.hart.overwatch.connection.request;

import jakarta.validation.constraints.NotNull;

public class CreateConnectionRequest {

    @NotNull
    private Long receiverId;

    @NotNull
    private Long senderId;

    public CreateConnectionRequest() {

    }

    public CreateConnectionRequest(Long receiverId, Long senderId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public Long getSenderId() {
        return senderId;
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

}
