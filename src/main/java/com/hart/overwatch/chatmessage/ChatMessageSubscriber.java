package com.hart.overwatch.chatmessage;

import org.springframework.data.redis.connection.Message;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.hart.overwatch.chatmessage.dto.ChatMessageDto;

@Service
public class ChatMessageSubscriber implements MessageListener {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {

        ChatMessageDto chatMessage = this.chatMessageService.createChatMessage(message.toString());

        if (chatMessage == null) {
            return;
        }


        ChatMessage chatMessageEntity =
                this.chatMessageService.getChatMessageById(chatMessage.getId());


        Long receiverId = chatMessageEntity.getConnection().getReceiver().getId();
        Long senderId = chatMessageEntity.getConnection().getSender().getId();

        this.messagingTemplate.convertAndSendToUser(String.valueOf(receiverId), "/topic/messages",
                chatMessage);
        this.messagingTemplate.convertAndSendToUser(String.valueOf(senderId), "/topic/messages",
                chatMessage);
    }
}
