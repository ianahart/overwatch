package com.hart.overwatch.setting.response;

import com.hart.overwatch.setting.dto.SettingDto;

public class UpdateSettingResponse {

    private String message;

    private SettingDto data;

    public UpdateSettingResponse() {

    }

    public UpdateSettingResponse(String message, SettingDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public SettingDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(SettingDto data) {
        this.data = data;
    }
}
