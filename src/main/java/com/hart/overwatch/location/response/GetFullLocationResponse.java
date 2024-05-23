package com.hart.overwatch.location.response;

import com.hart.overwatch.location.dto.LocationDto;

public class GetFullLocationResponse {

    private String message;

    private LocationDto data;

    public GetFullLocationResponse() {

    }

    public GetFullLocationResponse(String message, LocationDto data) {
        this.message = message;
        this.data =  data;
    }

    public String getMessage() {
        return message;
    }

    public LocationDto getData() {
        return data;
    }

    public void setData(LocationDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
