package com.hart.overwatch.profile.response;

import com.hart.overwatch.profile.AvatarDto;

public class UploadAvatarResponse {

    private String message;
    private AvatarDto data;

    public UploadAvatarResponse() {

    }

    public UploadAvatarResponse(String message, AvatarDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public AvatarDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setData(AvatarDto data) {
        this.data = data;
    }
}
