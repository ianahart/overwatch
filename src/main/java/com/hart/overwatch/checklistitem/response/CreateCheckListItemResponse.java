package com.hart.overwatch.checklistitem.response;

import com.hart.overwatch.checklistitem.dto.CheckListItemDto;

public class CreateCheckListItemResponse {

    private String message;

    private CheckListItemDto data;

    public CreateCheckListItemResponse() {

    }

    public CreateCheckListItemResponse(String message, CheckListItemDto data) {
        this.message = message;
        this.data  = data;
    }

    public String getMessage() {
        return message;
    }

    public CheckListItemDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(CheckListItemDto data) {
        this.data = data;
    }
}
