package com.hart.overwatch.setting.request;

import com.hart.overwatch.setting.dto.SettingDto;

public class UpdateSettingRequest {

    private SettingDto setting;

    public UpdateSettingRequest() {

    }

    public UpdateSettingRequest(SettingDto setting) {
        this.setting = setting;
    }

    public SettingDto getSetting() {
        return setting;
    }

    public void setSetting(SettingDto setting) {
        this.setting = setting;
    }
}
