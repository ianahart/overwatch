package com.hart.overwatch.connection;

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
import com.hart.overwatch.blockuser.BlockUserService;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.connection.dto.MinConnectionDto;
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
public class ConnectionServiceTest {


    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private BlockUserService blockUserService;

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

    private ConnectionDto senderConnectionDto;

    private ConnectionDto receiverConnectionDto;

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
        sender.setLocation(location);

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
        receiver.setLocation(location);

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

        receiverConnectionDto = new ConnectionDto(connection.getId(), receiver.getId(),
                sender.getId(), sender.getFirstName(), sender.getLastName(),
                sender.getProfile().getAvatarUrl(), sender.getProfile().getEmail(),
                sender.getLocation().getCity(), sender.getLocation().getCountry(),
                sender.getProfile().getContactNumber(), sender.getProfile().getBio());

        senderConnectionDto = new ConnectionDto(connection.getId(), receiver.getId(),
                sender.getId(), receiver.getFirstName(), receiver.getLastName(),
                receiver.getProfile().getAvatarUrl(), receiver.getProfile().getEmail(),
                receiver.getLocation().getCity(), receiver.getLocation().getCountry(),
                receiver.getProfile().getContactNumber(), receiver.getProfile().getBio());
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

    @Test
    public void ConnectionService_VerifyConnection_ReturnMinConnectionDto() {
      when(connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()))
            .thenReturn(connection);

      MinConnectionDto minConnectionDto = connectionService.verifyConnection(sender.getId(), receiver.getId());

      Assertions.assertThat(minConnectionDto).isNotNull();
      Assertions.assertThat(minConnectionDto.getId()).isEqualTo(connection.getId());
    }

    @Test
    public void ConnectionService_GetAllConnections_ThrowForbiddenException() {
        when(userService.getUserById(sender.getId())).thenReturn(sender);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(receiver);

        Assertions.assertThatThrownBy(() -> {
            connectionService.getAllConnections(sender.getId(), 0, 10, "next", "false");
        }).isInstanceOf(ForbiddenException.class).hasMessage("Cannot load another user's connections");
    }

    @Test
    public void ConnectionService_GetAllConnections_GetSenderConnectionsWithoutPins_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(senderConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(sender);
        connection.setChatMessages(List.of(chatMessage));
        List<Long> blockedUserIds = null;
        when(userService.getUserById(sender.getId())).thenReturn(sender);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(sender);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        when(connectionPinService.getOwnerConnectionPins(sender.getId())).thenReturn(List.of(1L));
        when(blockUserService.getBlockedUsersForCurUser(sender)).thenReturn(List.of());
        when(connectionRepository.getSenderConnectionsWithoutPins(pageable, sender.getId(),
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));


        PaginationDto<ConnectionDto> result =
                connectionService.getAllConnections(sender.getId(), 0, 10, "next", "true");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(senderConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName()).isEqualTo(senderConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(senderConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId()).isEqualTo(senderConnectionDto.getSenderId());
    }

    @Test
    public void ConnectionService_GetAllConnections_GetSenderConnections_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(senderConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(sender);
        connection.setChatMessages(List.of(chatMessage));
        List<Long> blockedUserIds = null;
        when(userService.getUserById(sender.getId())).thenReturn(sender);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(sender);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        when(connectionPinService.getOwnerConnectionPins(sender.getId())).thenReturn(List.of(1L));
        when(blockUserService.getBlockedUsersForCurUser(sender)).thenReturn(List.of());
        when(connectionRepository.getSenderConnections(pageable, sender.getId(), List.of(1L),
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));


        PaginationDto<ConnectionDto> result =
                connectionService.getAllConnections(sender.getId(), 0, 10, "next", "false");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(senderConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName()).isEqualTo(senderConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(senderConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId()).isEqualTo(senderConnectionDto.getSenderId());
    }

    @Test
    public void ConnectionService_GetAllConnections_GetReceiverConnections_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(receiverConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(sender);
        connection.setChatMessages(List.of(chatMessage));
        List<Long> blockedUserIds = null;
        when(userService.getUserById(receiver.getId())).thenReturn(receiver);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(receiver);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        when(connectionPinService.getOwnerConnectionPins(receiver.getId())).thenReturn(List.of(1L));
        when(blockUserService.getBlockedUsersForCurUser(receiver)).thenReturn(List.of());
        when(connectionRepository.getReceiverConnections(pageable, receiver.getId(), List.of(1L),
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));

        PaginationDto<ConnectionDto> result =
                connectionService.getAllConnections(receiver.getId(), 0, 10, "next", "false");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(receiverConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName())
                .isEqualTo(receiverConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(receiverConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId())
                .isEqualTo(receiverConnectionDto.getSenderId());
    }

    @Test
    public void ConnectionService_GetAllConnections_GetReceiverConnectionsWithoutPins_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(receiverConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(sender);
        connection.setChatMessages(List.of(chatMessage));
        List<Long> blockedUserIds = null;
        when(userService.getUserById(receiver.getId())).thenReturn(receiver);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(receiver);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        when(connectionPinService.getOwnerConnectionPins(receiver.getId())).thenReturn(List.of());
        when(blockUserService.getBlockedUsersForCurUser(receiver)).thenReturn(List.of());
        when(connectionRepository.getReceiverConnectionsWithoutPins(pageable, receiver.getId(),
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));


        PaginationDto<ConnectionDto> result =
                connectionService.getAllConnections(receiver.getId(), 0, 10, "next", "true");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(receiverConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName())
                .isEqualTo(receiverConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(receiverConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId())
                .isEqualTo(receiverConnectionDto.getSenderId());
    }



    @Test
    public void ConnectionService_GetAllSearchConnections_GetReceiver_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(receiverConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(receiver);
        connection.setChatMessages(List.of(chatMessage));
        List<Long> blockedUserIds = null;
        when(userService.getCurrentlyLoggedInUser()).thenReturn(receiver);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        when(blockUserService.getBlockedUsersForCurUser(receiver)).thenReturn(List.of());
        when(connectionRepository.getSearchReceiverConnections(pageable, receiver.getId(), "%john%",
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));

        PaginationDto<ConnectionDto> result =
                connectionService.getAllSearchConnections("john", 0, 10, "next");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(receiverConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName())
                .isEqualTo(receiverConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(receiverConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId())
                .isEqualTo(receiverConnectionDto.getSenderId());
    }

    @Test
    public void ConnectionService_GetAllSearchConnections_GetSender_ReturnPaginationDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConnectionDto> pageOfConnectionDto = new PageImpl<>(List.of(senderConnectionDto));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(sender);
        connection.setChatMessages(List.of(chatMessage));

        when(userService.getCurrentlyLoggedInUser()).thenReturn(sender);
        when(paginationService.getPageable(0, 10, "next")).thenReturn(pageable);
        List<Long> blockedUserIds = null;
        when(blockUserService.getBlockedUsersForCurUser(sender)).thenReturn(List.of());
        when(connectionRepository.getSearchSenderConnections(pageable, sender.getId(), "%jane%",
                blockedUserIds)).thenReturn(pageOfConnectionDto);
        when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));

        PaginationDto<ConnectionDto> result =
                connectionService.getAllSearchConnections("jane", 0, 10, "next");

        Assertions.assertThat(result).isNotNull();
        ConnectionDto resultDto = result.getItems().get(0);
        Assertions.assertThat(resultDto.getFirstName())
                .isEqualTo(senderConnectionDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName()).isEqualTo(senderConnectionDto.getLastName());
        Assertions.assertThat(resultDto.getReceiverId())
                .isEqualTo(senderConnectionDto.getReceiverId());
        Assertions.assertThat(resultDto.getSenderId()).isEqualTo(senderConnectionDto.getSenderId());
    }
}

