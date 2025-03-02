package com.hart.overwatch.teampinnedmessage.response;

import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;

import java.util.List;

public class ReorderTeamPinnedMessageResponse {

    private String message;

    private List<TeamPinnedMessageDto> data;


    public ReorderTeamPinnedMessageResponse() {

    }

    public ReorderTeamPinnedMessageResponse(String message, List<TeamPinnedMessageDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<TeamPinnedMessageDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<TeamPinnedMessageDto> data) {
        this.data = data;
    }

}
