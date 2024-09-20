package com.hart.overwatch.customfield.response;

import java.util.List;
import com.hart.overwatch.customfield.dto.CustomFieldDto;

public class GetCustomFieldsResponse {

    private String message;

    private List<CustomFieldDto> data;

    public GetCustomFieldsResponse() {

    }

    public GetCustomFieldsResponse(String message, List<CustomFieldDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<CustomFieldDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<CustomFieldDto> data) {
        this.data = data;
    }
}
