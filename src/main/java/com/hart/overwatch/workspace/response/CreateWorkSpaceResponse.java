package com.hart.overwatch.workspace.response;

import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;

public class CreateWorkSpaceResponse {

    private String message;
    private CreateWorkSpaceDto data;

    public CreateWorkSpaceResponse() {

    }

    public CreateWorkSpaceResponse(String message, CreateWorkSpaceDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public CreateWorkSpaceDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(CreateWorkSpaceDto data) {
        this.data = data;
    }
}
