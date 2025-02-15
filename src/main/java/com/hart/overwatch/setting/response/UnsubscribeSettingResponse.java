package com.hart.overwatch.setting.response;

public class UnsubscribeSettingResponse {

    private String message;

    public UnsubscribeSettingResponse() {

    }

    public UnsubscribeSettingResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
