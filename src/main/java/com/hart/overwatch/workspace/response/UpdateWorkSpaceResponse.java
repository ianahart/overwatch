package com.hart.overwatch.workspace.response;

import com.hart.overwatch.workspace.dto.WorkSpaceDto;

public class UpdateWorkSpaceResponse {

    private String message;

    private WorkSpaceDto data;


    public UpdateWorkSpaceResponse() {

    }

    public UpdateWorkSpaceResponse(String message, WorkSpaceDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public WorkSpaceDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(WorkSpaceDto data) {
        this.data = data;
    }
}
