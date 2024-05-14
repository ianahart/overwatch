package com.hart.overwatch.authentication.request;

public class VerifyOTPRequest {

    private Long userId;

    private String otpCode;


    public VerifyOTPRequest() {

    }

    public VerifyOTPRequest(Long userId, String otpCode) {
        this.userId = userId;
        this.otpCode = otpCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
