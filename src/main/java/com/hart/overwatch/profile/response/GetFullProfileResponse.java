package com.hart.overwatch.profile.response;

import com.hart.overwatch.profile.dto.FullProfileDto;

public class GetFullProfileResponse {

    private String message;

    private FullProfileDto data;


    public GetFullProfileResponse() {

    }

    public GetFullProfileResponse(String message, FullProfileDto data) {
        this.message = message;
        this.data = data;
    }

    public FullProfileDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(FullProfileDto data) {
        this.data = data;
    }
}
