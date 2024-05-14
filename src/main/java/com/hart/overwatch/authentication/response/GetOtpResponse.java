package com.hart.overwatch.authentication.response;

public class GetOtpResponse {

    private String message;

    private String otp;


    public GetOtpResponse() {

    }

    public GetOtpResponse(String message, String otp) {
        this.message = message;
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public String getMessage() {
        return message;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
