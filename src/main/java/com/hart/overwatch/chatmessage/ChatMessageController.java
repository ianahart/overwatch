package com.hart.overwatch.chatmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.chatmessage.response.GetAllChatMessageResponse;

@RestController
@RequestMapping(path = "/api/v1/chat-messages")
public class ChatMessageController {

    private ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllChatMessageResponse> getAllChatMessages(
            @RequestParam("connectionId") Long connectionId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllChatMessageResponse("success",
                this.chatMessageService.getChatMessages(connectionId)));
    }
}
