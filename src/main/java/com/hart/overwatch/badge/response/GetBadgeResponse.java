package com.hart.overwatch.badge.response;

import com.hart.overwatch.badge.dto.MinBadgeDto;

public class GetBadgeResponse {

    private String message;

    private MinBadgeDto data;


    public GetBadgeResponse() {

    }

    public GetBadgeResponse(String message, MinBadgeDto data) {
        this.message = message;
        this.data = data;
    }

    public MinBadgeDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(MinBadgeDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
