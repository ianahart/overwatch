package com.hart.overwatch.ban.response;

import com.hart.overwatch.ban.dto.BanDto;

public class UpdateBanResponse {

    private String message;

    private BanDto data;

    public UpdateBanResponse() {

    }

    public UpdateBanResponse(String message, BanDto data) {
        this.message = message;
        this.data = data;
    }

    public BanDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(BanDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
