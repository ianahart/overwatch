package com.hart.overwatch.setting.response;

public class UpdateSettingMfaEnabledResponse {

    private String message;

    private Boolean mfaEnabled;

    public UpdateSettingMfaEnabledResponse() {

    }

    public UpdateSettingMfaEnabledResponse(String message, boolean mfaEnabled) {
        this.message = message;
        this.mfaEnabled = mfaEnabled;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

}
