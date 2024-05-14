package com.hart.overwatch.user.request;

public class DeleteUserRequest {


    private String password;


    public DeleteUserRequest() {


    }


    public DeleteUserRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
