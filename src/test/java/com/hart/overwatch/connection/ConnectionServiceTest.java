package com.hart.overwatch.connection;

import static org.mockito.Mockito.*;
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
import com.hart.overwatch.connectionpin.ConnectionPinService;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {


    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    @Mock
    private ConnectionPinService connectionPinService;

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

        location.setId(1L);
        sender.setId(1L);
        profile.setId(1L);

        return sender;
    }

    private User createReceiver() {
        Boolean loggedIn = false;
        User receiver = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER,
                loggedIn, new Profile(), "Test12345%", new Setting());
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

        location.setId(2L);
        receiver.setId(2L);
        profile.setId(2L);

        return receiver;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();

        connection = new Connection(RequestStatus.ACCEPTED, sender, receiver);
        connection.setId(1L);
    }

    @Test
    public void ConnectionService_GetConnectionById_ReturnConnection() {
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));

        Connection returnedConnection = connectionService.getConnectionById(connection.getId());

        Assertions.assertThat(returnedConnection).isNotNull();
        Assertions.assertThat(returnedConnection.getId()).isEqualTo(connection.getId());
    }

    @Test
    public void ConnectionService_GetConnectionById_ThrowNotFoundException() {
        when(connectionRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> connectionService.getConnectionById(2L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(String.format("Connection with the id %d was not found", 2L));
    }

    @Test
    public void ConnectionService_CreateConnection_ReturnNothing() {
        when(connectionRepository.findExistingConnection(receiver.getId(), sender.getId()))
            .thenReturn(false);

        when(userService.getUserById(receiver.getId())).thenReturn(receiver);
        when(userService.getUserById(sender.getId())).thenReturn(sender);

        Connection newConnection = new Connection(RequestStatus.PENDING, sender, receiver);

        when(connectionRepository.save(any(Connection.class))).thenReturn(newConnection);

        connectionService.createConnection(receiver.getId(), sender.getId());

        verify(connectionRepository, times(1)).save(any(Connection.class));
    }

    @Test
    public void ConnectionService_CreateConnection_ThrowBadRequestException() {
       when(connectionRepository.findExistingConnection(receiver.getId(), sender.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> connectionService.createConnection(receiver.getId(), sender.getId()))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("You have already sent a connection request");
    }

    @Test
    public void ConnectionService_DeleteConnection_ReturnNothing() {
        when(connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId())).thenReturn(connection);
        doNothing().when(connectionRepository).delete(connection);

        Assertions.assertThatCode(() -> {
            connectionService.deleteConnection(sender.getId(), receiver.getId());
        }).doesNotThrowAnyException();

        verify(connectionRepository, times(1)).delete(connection);
    }

    @Test
    public void ConnectionService_UpdateConnectionStatus_ReturnNothing() {
        when(connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()))
            .thenReturn(connection);

        connection.setStatus(RequestStatus.ACCEPTED);
        when(connectionRepository.save(any(Connection.class))).thenReturn(connection);
        connectionService.updateConnectionStatus(sender.getId(), receiver.getId(), RequestStatus.ACCEPTED);

        verify(connectionRepository, times(1)).save(any(Connection.class));

    }


}

