package com.hart.overwatch.activity.response;

import com.hart.overwatch.activity.dto.ActivityDto;

public class CreateActivityResponse {

    private String message;

    private ActivityDto data;

    public CreateActivityResponse() {

    }

    public CreateActivityResponse(String message, ActivityDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public ActivityDto getData() {
        return data;
    }

    public void setData(ActivityDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
