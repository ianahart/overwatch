package com.hart.overwatch.topic.response;

import java.util.List;
import com.hart.overwatch.topic.dto.TopicDto;

public class GetTopicsResponse {

    private String message;

    private List<TopicDto> data;

    public GetTopicsResponse() {

    }

    public GetTopicsResponse(String message, List<TopicDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<TopicDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<TopicDto> data) {
        this.data = data;
    }
}
