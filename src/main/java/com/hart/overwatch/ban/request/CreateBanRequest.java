package com.hart.overwatch.ban.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateBanRequest {

    @NotNull(message = "Missing selected user")
    private Long userId;

    @NotNull(message = "Missing ban time")
    private Long time;

    @Size(min = 1, max = 300, message = "Admin notes must be between 1 and 300 characters")
    private String adminNotes;

    public CreateBanRequest() {

    }

    public CreateBanRequest(Long userId, Long time, String adminNotes) {
        this.userId = userId;
        this.time = time;
        this.adminNotes = adminNotes;
    }

    public Long getTime() {
        return time;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

}
