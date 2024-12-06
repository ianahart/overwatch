package com.hart.overwatch.user.dto;


public class MinUserDto {

    private Long id;

    private String fullName;

    private String avatarUrl;

    public MinUserDto() {

    }

    public MinUserDto(Long id, String fullName, String avatarUrl) {
        this.id = id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
