package com.hart.overwatch.phone.response;

import com.hart.overwatch.phone.dto.PhoneDto;

public class GetPhoneResponse {

    private String message;

    private PhoneDto data;


    public GetPhoneResponse() {

    }

    public GetPhoneResponse(String message, PhoneDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PhoneDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PhoneDto data) {
        this.data = data;
    }
}
