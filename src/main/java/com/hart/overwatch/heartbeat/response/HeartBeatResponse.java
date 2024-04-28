package com.hart.overwatch.heartbeat.response;

public class HeartBeatResponse {

    private String message;

    public HeartBeatResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
