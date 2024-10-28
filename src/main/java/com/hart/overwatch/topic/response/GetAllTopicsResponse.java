package com.hart.overwatch.topic.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.topic.dto.TopicDto;

public class GetAllTopicsResponse {

    private String message;

    private PaginationDto<TopicDto> data;

    public GetAllTopicsResponse() {

    }

    public GetAllTopicsResponse(String message, PaginationDto<TopicDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TopicDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TopicDto> data) {
        this.data = data;
    }

}
