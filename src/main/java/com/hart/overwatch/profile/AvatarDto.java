package com.hart.overwatch.profile;

public class AvatarDto {

    private String avatarUrl;


    public AvatarDto() {

    }

    public AvatarDto(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
