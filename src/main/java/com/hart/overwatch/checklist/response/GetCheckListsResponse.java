package com.hart.overwatch.checklist.response;

import java.util.List;
import com.hart.overwatch.checklist.dto.CheckListDto;

public class GetCheckListsResponse {

    private String message;

    private List<CheckListDto> data;

    public GetCheckListsResponse() {

    }

    public GetCheckListsResponse(String message, List<CheckListDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<CheckListDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<CheckListDto> data) {
        this.data = data;
    }
}
