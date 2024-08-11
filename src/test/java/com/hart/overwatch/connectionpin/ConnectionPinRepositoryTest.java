package com.hart.overwatch.connectionpin;


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
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.location.LocationRepository;
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
@Sql(scripts = "classpath:reset_connection_pin_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ConnectionPinRepositoryTest {


    @Autowired
    private ConnectionPinRepository connectionPinRepository;

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

    private ConnectionPin connectionPin;

    private User owner;

    private User pinned;

    private User createOwner() {
        Boolean loggedIn = false;
        User owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo.jpeg");
        profile.setEmail("john@mail.com");
        profile.setContactNumber("4444444444");
        profile.setBio("This is a bio");
        owner.setProfile(profile);
        profile.setUser(owner);
        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(owner);

        locationRepository.save(location);
        userRepository.save(owner);
        profileRepository.save(profile);

        return owner;
    }

    private User createPinned() {
        Boolean loggedIn = false;
        User pinned = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo-2.jpeg");
        profile.setEmail("jane@mail.com");
        profile.setContactNumber("5555555555");
        profile.setBio("This is a bio");
        pinned.setProfile(profile);
        profile.setUser(pinned);
        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(pinned);

        locationRepository.save(location);
        userRepository.save(pinned);
        profileRepository.save(profile);

        return pinned;
    }

    private Connection createConnection(User owner, User pinned) {
        Connection connection = new Connection(RequestStatus.ACCEPTED, owner, pinned);
        connectionRepository.save(connection);

        return connection;
    }

    private ConnectionPin createConnectionPin(Connection connection, User owner, User pinned) {
        ConnectionPin connectionPin = new ConnectionPin(connection, owner, pinned);
        connectionPinRepository.save(connectionPin);

        return connectionPin;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        pinned = createPinned();
        connection = createConnection(owner, pinned);
        connectionPin = createConnectionPin(connection, owner, pinned);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        connectionPinRepository.deleteAll();
        connectionRepository.deleteAll();
        locationRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

}


