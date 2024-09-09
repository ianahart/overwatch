package com.hart.overwatch.activelabel.response;

import java.util.List;
import com.hart.overwatch.activelabel.dto.ActiveLabelDto;


public class GetActiveLabelResponse {
    private String message;

    private List<ActiveLabelDto> data;


    public GetActiveLabelResponse() {

    }

    public GetActiveLabelResponse(String message, List<ActiveLabelDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<ActiveLabelDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<ActiveLabelDto> data) {
        this.data = data;
    }
}
