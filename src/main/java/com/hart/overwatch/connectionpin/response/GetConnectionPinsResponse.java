package com.hart.overwatch.connectionpin.response;

import java.util.List;
import com.hart.overwatch.connectionpin.dto.ConnectionPinDto;

public class GetConnectionPinsResponse {

    private String message;

    private List<ConnectionPinDto> data;


    public GetConnectionPinsResponse() {

    }

    public GetConnectionPinsResponse(String message, List<ConnectionPinDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<ConnectionPinDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<ConnectionPinDto> data) {
        this.data = data;
    }
}
