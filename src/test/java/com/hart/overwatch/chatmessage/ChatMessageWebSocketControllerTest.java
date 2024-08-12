package com.hart.overwatch.chatmessage;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class ChatMessageWebSocketControllerTest {

    @InjectMocks
    private ChatMessageWebSocketController chatMessageWebSocketController;

    @Mock
    private ChatMessagePublisher chatMessagePublisher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessage() {
        String message = "Hello, WebSocket!";

        String result = chatMessageWebSocketController.sendMessage(message);

        verify(chatMessagePublisher).sendMessage(message);

        assertEquals(message, result, "The message should be sent and returned correctly.");
    }
}
