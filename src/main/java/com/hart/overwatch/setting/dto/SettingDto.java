package com.hart.overwatch.setting.dto;

import java.sql.Timestamp;

public class SettingDto {

    private Long id;

    private Long userId;

    private Boolean mfaEnabled;

    private Timestamp createdAt;


    public SettingDto() {

    }

    public SettingDto(Long id, Long userId, Boolean mfaEnabled, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
