package com.hart.overwatch.chatmessage;

import java.util.List;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.blockuser.BlockUser;
import com.hart.overwatch.blockuser.BlockUserService;
import com.hart.overwatch.chatmessage.dto.ChatMessageDto;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserService userService;

    private final ConnectionService connectionService;

    private final BlockUserService blockUserService;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserService userService,
            ConnectionService connectionService, BlockUserService blockUserService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
        this.connectionService = connectionService;
        this.blockUserService = blockUserService;
    }


    public ChatMessage getChatMessageById(Long chatMessageId) {
        return this.chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find a chat message with the id %d", chatMessageId)));
    }


    private ChatMessageDto constructFullChatMessage(Long chatMessageId) {
        try {
            return this.chatMessageRepository.getChatMessage(chatMessageId);

        } catch (DataAccessException ex) {
            return null;
        }
    }

    public ChatMessageDto createChatMessage(String messageJson) {

        JSONObject message = new JSONObject(messageJson);

        Long connectionId = Long.valueOf(message.getLong("connectionId"));
        Long userId = Long.valueOf(message.getLong("userId"));
        String text = message.getString("text");

        User user = this.userService.getUserById(userId);

        Connection connection = this.connectionService.getConnectionById(connectionId);

        ChatMessage chatMessage = new ChatMessage(text, user, connection);

        Long potentialBlockerId = user.getId().equals(connection.getReceiver().getId())
                ? connection.getSender().getId()
                : connection.getReceiver().getId();

        BlockUser blockedByReceiver =
                blockUserService.getCurUserBlockedByUser(user.getId(), potentialBlockerId);

        if (blockedByReceiver != null) {
            return null;
        }
        this.chatMessageRepository.save(chatMessage);

        return constructFullChatMessage(chatMessage.getId());
    }

    public List<ChatMessageDto> getChatMessages(Long connectionId) {
        return this.chatMessageRepository.getChatMessages(connectionId);
    }

}
