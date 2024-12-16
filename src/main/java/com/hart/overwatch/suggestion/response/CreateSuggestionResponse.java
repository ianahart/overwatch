package com.hart.overwatch.suggestion.response;

public class CreateSuggestionResponse {

    private String message;

    public CreateSuggestionResponse() {

    }

    public CreateSuggestionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
