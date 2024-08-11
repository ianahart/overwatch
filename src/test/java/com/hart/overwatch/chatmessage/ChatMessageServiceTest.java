package com.hart.overwatch.chatmessage;

import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.chatmessage.dto.ChatMessageDto;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.connectionpin.dto.ConnectionPinDto;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {


    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    private Connection connection;

    private List<ChatMessage> chatMessages;

    private ChatMessageDto chatMessageDto;

    private User sender;

    private User receiver;

    private User createSender() {
        Boolean loggedIn = false;
        User sender = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo.jpeg");
        profile.setEmail("john@mail.com");
        profile.setContactNumber("4444444444");
        profile.setBio("This is a bio");
        sender.setProfile(profile);
        profile.setUser(sender);
        sender.setId(1L);
        profile.setId(1L);

        return sender;
    }

    private User createReceiver() {
        Boolean loggedIn = false;
        User receiver = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo-2.jpeg");
        profile.setEmail("jane@mail.com");
        profile.setContactNumber("5555555555");
        profile.setBio("This is a bio");
        receiver.setProfile(profile);
        profile.setUser(receiver);

        receiver.setId(2L);
        profile.setId(2L);

        return receiver;
    }

    private Connection createConnection(User sender, User receiver) {
        Connection connection = new Connection(RequestStatus.ACCEPTED, sender, receiver);
        connection.setId(1L);

        return connection;
    }

    private List<ChatMessage> createChatMessages(Connection connection, User sender) {
        List<String> messageTexts = List.of("hi", "hello", "hello there", "good morning");
        List<ChatMessage> chatMessages = new ArrayList<>();

        for (int i = 0; i < messageTexts.size(); i++) {
            ChatMessage newMessage = new ChatMessage(messageTexts.get(i), sender, connection);
            newMessage.setId(Long.valueOf(i + 1));
            chatMessages.add(newMessage);
        }
        return chatMessages;
    }

    private ChatMessageDto createChatMessageDto(User sender, Connection connection,
            List<ChatMessage> chatMessages) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        ChatMessage firstMessage =
                chatMessages.size() > 0 ? chatMessages.get(0) : new ChatMessage();

        return new ChatMessageDto(firstMessage.getId(), sender.getFirstName(), sender.getLastName(),
                createdAt, firstMessage.getText(), sender.getProfile().getAvatarUrl(),
                connection.getId(), sender.getId());
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();
        connection = createConnection(sender, receiver);
        chatMessages = createChatMessages(connection, sender);
        chatMessageDto = createChatMessageDto(sender, connection, chatMessages);
    }

    @Test
    public void ChatMessageService_GetChatMessageById_ReturnChatMessage() {
        when(chatMessageRepository.findById(chatMessages.get(0).getId()))
            .thenReturn(Optional.of(chatMessages.get(0)));

        ChatMessage actualChatMessage = chatMessageService.getChatMessageById(chatMessages.get(0).getId());

        Assertions.assertThat(actualChatMessage).isNotNull();
        Assertions.assertThat(actualChatMessage.getId()).isEqualTo(chatMessages.get(0).getId());
    }

    @Test
    public void ChatMessageService_GetChatMessageById_ThrowNotFoundException() {
        when(chatMessageRepository.findById(999L)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
           chatMessageService.getChatMessageById(999L);
        })
            .isInstanceOf(NotFoundException.class)
            .hasMessage(String.format("Could not find a chat message with the id %d", 999L));
    }

}


