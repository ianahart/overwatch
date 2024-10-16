package com.hart.overwatch.blockuser.request;

import jakarta.validation.constraints.NotNull;

public class CreateBlockUserRequest {

    @NotNull
    private Long blockedUserId;

    @NotNull
    private Long blockerUserId;

    public CreateBlockUserRequest() {

    }

    public CreateBlockUserRequest(Long blockedUserId, Long blockerUserId) {
        this.blockedUserId = blockedUserId;
        this.blockerUserId = blockerUserId;
    }

    public Long getBlockedUserId() {
        return blockedUserId;
    }

    public Long getBlockerUserId() {
        return blockerUserId;
    }

    public void setBlockedUserId(Long blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public void setBlockerUserId(Long blockerUserId) {
        this.blockerUserId = blockerUserId;
    }
}
