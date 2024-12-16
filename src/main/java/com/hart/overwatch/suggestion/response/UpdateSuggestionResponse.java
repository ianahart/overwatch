package com.hart.overwatch.suggestion.response;

import com.hart.overwatch.suggestion.FeedbackStatus;

public class UpdateSuggestionResponse {

    private String message;

    private FeedbackStatus data;


    public UpdateSuggestionResponse() {

    }

    public UpdateSuggestionResponse(String message, FeedbackStatus data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackStatus getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(FeedbackStatus data) {
        this.data = data;
    }
}
