package com.hart.overwatch.chatmessage;

import com.hart.overwatch.chatmessage.dto.ChatMessageDto;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatMessageService chatMessageService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    private List<ChatMessage> chatMessages;

    private ChatMessageDto chatMessageDto;

    private User sender;

    private User receiver;

    private Connection connection;

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
        chatMessageDto.setId(1L);
    }

    @Test
    public void ChatMessageControllerTest_GetAllChatMessages_ReturnGetAllChatMessagesResponse() throws Exception {
        when(chatMessageService.getChatMessages(connection.getId())).thenReturn(List.of(chatMessageDto));

        ResultActions response = mockMvc
        .perform(get("/api/v1/chat-messages")
            .contentType(MediaType.APPLICATION_JSON)
            .param("connectionId", String.valueOf(connection.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(chatMessageDto.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName", CoreMatchers.is(chatMessageDto.getFirstName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName", CoreMatchers.is(chatMessageDto.getLastName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].text", CoreMatchers.is(chatMessageDto.getText())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].avatarUrl", CoreMatchers.is(chatMessageDto.getAvatarUrl())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].connectionId", CoreMatchers.is(chatMessageDto.getConnectionId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId", CoreMatchers.is(chatMessageDto.getUserId().intValue())));

    }

}

