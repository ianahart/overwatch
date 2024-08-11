package com.hart.overwatch.connectionpin;

import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.connectionpin.ConnectionPinService;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ConnectionPinServiceTest {


    @InjectMocks
    private ConnectionPinService connectionPinService;

    @Mock
    private ConnectionPinRepository connectionPinRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private Connection connection;

    private ConnectionPin connectionPin;

    private User owner;

    private User pinned;


    private User createOwner() {
        Boolean loggedIn = false;
        User owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        owner.setId(1L);
        profile.setId(1L);
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo.jpeg");
        profile.setEmail("john@mail.com");
        profile.setContactNumber("4444444444");
        profile.setBio("This is a bio");
        owner.setProfile(profile);
        profile.setUser(owner);
        Location location = new Location();
        location.setId(1L);
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(owner);
        owner.setLocation(location);

        return owner;
    }

    private User createPinned() {
        Boolean loggedIn = false;
        User pinned = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        Profile profile = new Profile();
        pinned.setId(2L);
        profile.setId(2L);
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo-2.jpeg");
        profile.setEmail("jane@mail.com");
        profile.setContactNumber("5555555555");
        profile.setBio("This is a bio");
        pinned.setProfile(profile);
        profile.setUser(pinned);
        Location location = new Location();
        location.setId(2L);
        location.setCountry("United States");
        location.setCity("New York City");
        location.setUser(pinned);
        pinned.setLocation(location);


        return pinned;
    }

    private Connection createConnection(User owner, User pinned) {
        Connection connection = new Connection(RequestStatus.ACCEPTED, owner, pinned);
        connection.setId(1L);

        return connection;
    }

    private ConnectionPin createConnectionPin(Connection connection, User owner, User pinned) {
        ConnectionPin connectionPin = new ConnectionPin(connection, owner, pinned);
        connectionPin.setId(1L);

        return connectionPin;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        pinned = createPinned();
        connection = createConnection(owner, pinned);
        connectionPin = createConnectionPin(connection, owner, pinned);
    }

    @Test
    public void ConnectionPinService_GetConnectionPinById_ThrowNotFoundException() {
        Long nonExistentConnectionPinId = 999L;
        when(connectionPinRepository.findById(nonExistentConnectionPinId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            connectionPinService.getConnectionPinById(nonExistentConnectionPinId);
        }).isInstanceOf(NotFoundException.class).hasMessage(String
                .format("Cannot find connection pin with the id %d", nonExistentConnectionPinId));
    }

    @Test
    public void ConnectionPinService_GetConnectionPinById_ReturnConnectionPin() {
        when(connectionPinRepository.findById(connectionPin.getId())).thenReturn(Optional.of(connectionPin));

        ConnectionPin actualConnectionPin = connectionPinService.getConnectionPinById(connectionPin.getId());

        Assertions.assertThat(actualConnectionPin).isNotNull();
        Assertions.assertThat(actualConnectionPin.getId()).isEqualTo(connectionPin.getId());
    }

    @Test
    public void ConnectionPinService_GetOwnerConnectionPins_ReturnListOfConnectionPinIds() {
        List<Long> expectedConnectionPinIds =
                List.of(connectionPin).stream().map(cp -> cp.getId()).toList();
        when(connectionPinRepository.findConnectionPinByOwnerId(owner.getId()))
                .thenReturn(List.of(connectionPin));

        List<Long> actualConnectionPinIds =
                connectionPinService.getOwnerConnectionPins(owner.getId());

        Assertions.assertThat(actualConnectionPinIds).isNotNull();
        Assertions.assertThat(actualConnectionPinIds).isEqualTo(expectedConnectionPinIds);

    }
}


