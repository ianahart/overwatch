package com.hart.overwatch.label.response;

import java.util.List;
import com.hart.overwatch.label.dto.LabelDto;

public class GetLabelsResponse {

    private String message;

    private List<LabelDto> data;


    public GetLabelsResponse() {

    }

    public GetLabelsResponse(String message, List<LabelDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<LabelDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<LabelDto> data) {
        this.data = data;
    }
}
