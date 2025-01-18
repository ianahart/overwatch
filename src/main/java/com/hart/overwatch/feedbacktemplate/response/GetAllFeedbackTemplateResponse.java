package com.hart.overwatch.feedbacktemplate.response;

import java.util.List;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;

public class GetAllFeedbackTemplateResponse {

    private String message;

    private List<MinFeedbackTemplateDto> data;

    public GetAllFeedbackTemplateResponse() {

    }

    public GetAllFeedbackTemplateResponse(String message, List<MinFeedbackTemplateDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<MinFeedbackTemplateDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<MinFeedbackTemplateDto> data) {
        this.data = data;
    }
}
