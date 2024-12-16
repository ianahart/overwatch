package com.hart.overwatch.suggestion.request;

import com.hart.overwatch.suggestion.FeedbackStatus;

public class UpdateSuggestionRequest {

    private FeedbackStatus feedbackStatus;

    public UpdateSuggestionRequest() {

    }

    public UpdateSuggestionRequest(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }
}
