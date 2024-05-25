package com.hart.overwatch.profile.request;

public class RemoveAvatarRequest {

    private String avatarUrl;

    private String avatarFilename;

    public RemoveAvatarRequest(String avatarUrl, String avatarFilename) {
        this.avatarUrl = avatarUrl;
        this.avatarFilename = avatarFilename;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setAvatarFilename(String avatarFilename) {
        this.avatarFilename = avatarFilename;
    }

}
