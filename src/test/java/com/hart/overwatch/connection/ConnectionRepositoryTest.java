package com.hart.overwatch.connection;


import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.location.LocationRepository;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_connection_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ConnectionRepositoryTest {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;


    private Connection connection;

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
        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(sender);

        locationRepository.save(location);
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
        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(receiver);

        locationRepository.save(location);
        userRepository.save(receiver);
        profileRepository.save(profile);

        return receiver;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();

        connection = new Connection(RequestStatus.ACCEPTED, sender, receiver);

        connectionRepository.save(connection);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        connectionRepository.deleteAll();
        locationRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ConnectionRepository_GetSearchSenderConnections_ReturnPageOfConnectionDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository.getSearchSenderConnections(pageable,
                sender.getId(), "jane", blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);
        ConnectionDto connectionDto = result.getContent().get(0);
        Assertions.assertThat(connectionDto.getReceiverId()).isEqualTo(receiver.getId());
        Assertions.assertThat(connectionDto.getSenderId()).isEqualTo(sender.getId());
    }

    @Test
    public void ConnectionRepository_GetSearchReceiverConnections_ReturnPageOfConnectionDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository.getSearchReceiverConnections(pageable,
                receiver.getId(), "john", blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);
        ConnectionDto connectionDto = result.getContent().get(0);
        Assertions.assertThat(connectionDto.getReceiverId()).isEqualTo(receiver.getId());
        Assertions.assertThat(connectionDto.getSenderId()).isEqualTo(sender.getId());
    }

    @Test
    public void ConnectionRepository_GetSenderConnections_ReturnPageOfConnectionDtoOfSizeZero() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> connectionPinIds = Arrays.asList(connection.getId());
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository.getSenderConnections(pageable,
                sender.getId(), connectionPinIds, blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    public void ConnectionRepository_GetSenderConnectionsWithoutPins_ReturnPageOfConnectionDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository.getSenderConnectionsWithoutPins(pageable,
                sender.getId(), blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);
        ConnectionDto connectionDto = result.getContent().get(0);
        Assertions.assertThat(connectionDto.getSenderId()).isEqualTo(sender.getId());
        Assertions.assertThat(connectionDto.getReceiverId()).isEqualTo(receiver.getId());
    }

    @Test
    public void ConnectionRepository_GetReceiverConnections_ReturnPageOfConnectionDtoOfSizeZero() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> connectionPinIds = Arrays.asList(connection.getId());
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository.getReceiverConnections(pageable,
                receiver.getId(), connectionPinIds, blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    public void ConnectionRepository_GetReceiverConnectionsWithoutPins_ReturnPageOfConnectionDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> blockedUserIds = null;
        Page<ConnectionDto> result = connectionRepository
                .getReceiverConnectionsWithoutPins(pageable, receiver.getId(), blockedUserIds);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);
        ConnectionDto connectionDto = result.getContent().get(0);
        Assertions.assertThat(connectionDto.getSenderId()).isEqualTo(sender.getId());
        Assertions.assertThat(connectionDto.getReceiverId()).isEqualTo(receiver.getId());
    }

    @Test
    public void ConnectionRepository_FindExistingConnection_ReturnBooleanTrue() {
        boolean existingConnection =
                connectionRepository.findExistingConnection(receiver.getId(), sender.getId());

        Assertions.assertThat(existingConnection).isTrue();
    }
}

