package com.hart.overwatch.comment.response;

import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllCommentResponse {

    private String message;

    private PaginationDto<CommentDto> data;

    public GetAllCommentResponse() {

    }

    public GetAllCommentResponse(String message, PaginationDto<CommentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<CommentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<CommentDto> data) {
        this.data = data;
    }


}
