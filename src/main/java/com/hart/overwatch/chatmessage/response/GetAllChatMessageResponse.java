package com.hart.overwatch.chatmessage.response;

import java.util.List;
import com.hart.overwatch.chatmessage.dto.ChatMessageDto;

public class GetAllChatMessageResponse {

    private String message;

    private List<ChatMessageDto> data;


    public GetAllChatMessageResponse() {

    }

    public GetAllChatMessageResponse(String message, List<ChatMessageDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<ChatMessageDto> getData() {
        return data;
    }
}
