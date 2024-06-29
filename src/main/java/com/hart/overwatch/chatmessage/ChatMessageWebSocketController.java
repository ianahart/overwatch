package com.hart.overwatch.chatmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageWebSocketController {

    @Autowired
    private ChatMessagePublisher chatMessagePublisher;

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public String sendMessage(@Payload String chatMessage) {

        chatMessagePublisher.sendMessage(chatMessage);
        return chatMessage;
    }
}
