package com.hart.overwatch.passwordreset.request;

public class PasswordResetRequest {

    private String token;

    private String passCode;

    private String password;

    private String confirmPassword;


    public PasswordResetRequest() {

    }

    public PasswordResetRequest(String token, String passCode, String password,
            String confirmPassword) {
        this.token = token;
        this.passCode = passCode;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public String getPassCode() {
        return passCode;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
