package com.hart.overwatch.ban.dto;

import java.time.LocalDateTime;

public class BanDto {

    private Long id;

    private String fullName;

    private Long userId;

    private Long time;

    private String adminNotes;

    private LocalDateTime createdAt;

    private LocalDateTime banDate;

    private String email;

    public BanDto() {

    }

    public BanDto(Long id, String fullName, Long userId, Long time, String adminNotes,
            LocalDateTime createdAt, LocalDateTime banDate, String email) {
        this.id = id;
        this.fullName = fullName;
        this.userId = userId;
        this.time = time;
        this.adminNotes = adminNotes;
        this.createdAt = createdAt;
        this.banDate = banDate;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getTime() {
        return time;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public LocalDateTime getBanDate() {
        return banDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setBanDate(LocalDateTime banDate) {
        this.banDate = banDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
