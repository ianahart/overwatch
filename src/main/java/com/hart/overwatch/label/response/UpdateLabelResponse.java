package com.hart.overwatch.label.response;

import com.hart.overwatch.label.dto.LabelDto;

public class UpdateLabelResponse {

    private String message;

    private LabelDto data;


    public UpdateLabelResponse() {

    }

    public UpdateLabelResponse(String message, LabelDto data) {
        this.message = message;
        this.data = data;
    }

    public LabelDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(LabelDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
