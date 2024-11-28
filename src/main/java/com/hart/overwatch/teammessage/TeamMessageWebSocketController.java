package com.hart.overwatch.teammessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TeamMessageWebSocketController {

    @Autowired
    private TeamMessagePublisher teamMessagePublisher;

    @MessageMapping("/team-messages")
    @SendTo("/topic/team-messages")
    public String sendMessage(@Payload String teamMessage) {

        teamMessagePublisher.sendMessage(teamMessage);
        return teamMessage;
    }
}

