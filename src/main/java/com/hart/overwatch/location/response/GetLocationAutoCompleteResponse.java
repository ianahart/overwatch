package com.hart.overwatch.location.response;

public class GetLocationAutoCompleteResponse {

    private String message;

    private String data;

    public GetLocationAutoCompleteResponse() {

    }

    public GetLocationAutoCompleteResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
