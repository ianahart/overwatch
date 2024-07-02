package com.hart.overwatch.favorite.response;


public class ToggleFavoriteResponse {

    private String message;


    public ToggleFavoriteResponse() {

    }

    public ToggleFavoriteResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
