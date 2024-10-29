package com.hart.overwatch.replycomment.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.replycomment.dto.ReplyCommentDto;

public class GetReplyCommentsByUserAndCommentResponse {

    private String message;

    private PaginationDto<ReplyCommentDto> data;

    public GetReplyCommentsByUserAndCommentResponse() {

    }

    public GetReplyCommentsByUserAndCommentResponse(String message,
            PaginationDto<ReplyCommentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ReplyCommentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ReplyCommentDto> data) {
        this.data = data;
    }

}
