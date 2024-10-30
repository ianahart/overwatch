package com.hart.overwatch.savecomment.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;

public class GetSaveCommentsResponse {

    private String message;

    private PaginationDto<SaveCommentDto> data;

    public GetSaveCommentsResponse() {

    }

    public GetSaveCommentsResponse(String message, PaginationDto<SaveCommentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(PaginationDto<SaveCommentDto> data) {
        this.data = data;
    }

    public PaginationDto<SaveCommentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
