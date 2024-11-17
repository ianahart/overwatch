package com.hart.overwatch.reportcomment.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.reportcomment.dto.ReportCommentDto;

public class GetReportCommentsResponse {

    private String message;

    private PaginationDto<ReportCommentDto> data;


    public GetReportCommentsResponse() {

    }

    public GetReportCommentsResponse(String message, PaginationDto<ReportCommentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ReportCommentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ReportCommentDto> data) {
        this.data = data;
    }
}
