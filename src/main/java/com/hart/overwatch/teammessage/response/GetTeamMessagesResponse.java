package com.hart.overwatch.teammessage.response;

import java.util.List;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;

public class GetTeamMessagesResponse {

    private String message;

    private List<TeamMessageDto> data;


    public GetTeamMessagesResponse() {

    }

    public GetTeamMessagesResponse(String message, List<TeamMessageDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<TeamMessageDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<TeamMessageDto> data) {
        this.data = data;
    }
}
