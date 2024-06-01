package com.hart.overwatch.paymentmethod.response;

import com.hart.overwatch.paymentmethod.dto.UserPaymentMethodDto;

public class GetUserPaymentMethodResponse {

    private String message;

    private UserPaymentMethodDto data;


    public GetUserPaymentMethodResponse() {

    }

    public GetUserPaymentMethodResponse(String message, UserPaymentMethodDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public UserPaymentMethodDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(UserPaymentMethodDto data) {
        this.data = data;
    }
}
