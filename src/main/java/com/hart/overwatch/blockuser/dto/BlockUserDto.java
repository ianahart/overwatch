package com.hart.overwatch.blockuser.dto;

import java.time.LocalDateTime;

public class BlockUserDto {

    private Long id;

    private Long blockedUserId;

    private LocalDateTime createdAt;

    private String fullName;

    private String avatarUrl;


    public BlockUserDto() {

    }

    public BlockUserDto(Long id, Long blockedUserId, LocalDateTime createdAt, String fullName,
            String avatarUrl) {
        this.id = id;
        this.blockedUserId = blockedUserId;
        this.createdAt = createdAt;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getBlockedUserId() {
        return blockedUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setBlockedUserId(Long blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

