package com.hart.overwatch.comment.response;

import com.hart.overwatch.comment.dto.MinCommentDto;

public class GetCommentResponse {

    private String message;

    private MinCommentDto data;

    public GetCommentResponse() {

    }

    public GetCommentResponse(String message, MinCommentDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public MinCommentDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(MinCommentDto data) {
        this.data = data;
    }


}
