package com.hart.overwatch.connection.response;

import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.connection.dto.MinConnectionDto;

public class VerifyConnectionResponse {

    private String message;

    private MinConnectionDto data;


    public VerifyConnectionResponse() {

    }

    public VerifyConnectionResponse(String message, MinConnectionDto data) {
        this.message = message;
        this.data  = data;
    }

    public String getMessage() {
        return message;
    }

    public MinConnectionDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(MinConnectionDto data) {
        this.data = data;
    }
}
