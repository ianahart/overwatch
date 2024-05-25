package com.hart.overwatch.profile.request;

import org.springframework.web.multipart.MultipartFile;

public class UploadAvatarRequest {

    private MultipartFile avatar;


    public UploadAvatarRequest() {

    }

    public UploadAvatarRequest(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
