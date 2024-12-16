package com.hart.overwatch.suggestion.response;

public class DeleteSuggestionResponse {

    private String message;

    public DeleteSuggestionResponse() {

    }

    public DeleteSuggestionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
