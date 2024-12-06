package com.hart.overwatch.ban.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateBanRequest {

    @Size(min = 1, max = 300, message = "Admin notes must be between 1 and 300 characters")
    private String adminNotes;

    @NotNull(message = "Please select a time")
    private Long time;

    public UpdateBanRequest() {

    }

    public UpdateBanRequest(String adminNotes, Long time) {
        this.adminNotes = adminNotes;
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
