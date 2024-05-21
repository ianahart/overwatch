package com.hart.overwatch.user.response;

import com.hart.overwatch.user.dto.UpdateUserDto;

public class UpdateUserResponse {

    private String message;

    private UpdateUserDto data;

    public UpdateUserResponse() {

    }

    public UpdateUserResponse(String message, UpdateUserDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public UpdateUserDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(UpdateUserDto data) {
        this.data = data;
    }

}
