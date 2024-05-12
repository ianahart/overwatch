package com.hart.overwatch.user.request;

public class UpdateUserPasswordRequest {

    private String currentPassword;

    private String newPassword;


    public UpdateUserPasswordRequest() {

    }

    public UpdateUserPasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
