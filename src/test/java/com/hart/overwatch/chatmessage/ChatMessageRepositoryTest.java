package com.hart.overwatch.chatmessage;

import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.connection.ConnectionRepository;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_chat_message_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ChatMessageRepositoryTest {


    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;


    private Connection connection;

    private List<ChatMessage> chatMessages;

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

        userRepository.save(sender);
        profileRepository.save(profile);

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

        userRepository.save(receiver);
        profileRepository.save(profile);

        return receiver;
    }

    private Connection createConnection(User sender, User receiver) {
        Connection connection = new Connection(RequestStatus.ACCEPTED, sender, receiver);
        connectionRepository.save(connection);

        return connection;
    }

    private List<ChatMessage> createChatMessages(Connection connection, User sender) {
        List<String> messageTexts = List.of("hi", "hello", "hello there", "good morning");
        List<ChatMessage> chatMessages = new ArrayList<>();

        for (String messageText : messageTexts) {
            chatMessages.add(new ChatMessage(messageText, sender, connection));
        }
        this.chatMessageRepository.saveAll(chatMessages);
        return chatMessages;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();
        connection = createConnection(sender, receiver);
        chatMessages = createChatMessages(connection, sender);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        chatMessageRepository.deleteAll();
        connectionRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }
}


