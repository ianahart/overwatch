package com.hart.overwatch.topic.response;

import com.hart.overwatch.topic.dto.TopicDto;

public class GetTopicResponse {

    private String message;

    private TopicDto data;

    public GetTopicResponse() {

    }

    public GetTopicResponse(String message, TopicDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public TopicDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(TopicDto data) {
        this.data = data;
    }
}
