package com.hart.overwatch.setting.request;

public class UpdateSettingMfaEnabledRequest {

    private Boolean mfaEnabled;


    public UpdateSettingMfaEnabledRequest() {

    }

    public UpdateSettingMfaEnabledRequest(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
