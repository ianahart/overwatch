package com.hart.overwatch.connectionpin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.connectionpin.dto.ConnectionPinDto;
import com.hart.overwatch.connectionpin.request.CreateConnectionPinRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.location.Location;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = ConnectionPinController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ConnectionPinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConnectionPinService connectionPinService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void ConnectionPinController_CreateConnection_ReturnCreateConnectionPinResponse()
            throws Exception {
        CreateConnectionPinRequest request =
                new CreateConnectionPinRequest(pinned.getId(), owner.getId(), connection.getId());

        doNothing().when(connectionPinService).createConnectionPin(request.getOwnerId(),
                request.getPinnedId(), request.getConnectionId());

        ResultActions response = mockMvc
                .perform(post("/api/v1/connection-pins").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void ConnectionPinControllerTest_GetConnectionPins_ReturnGetConnectionPinsResponse()
            throws Exception {
        ConnectionPinDto expectedConnectionPinDto =
                new ConnectionPinDto(connection.getId(), connectionPin.getId(), pinned.getId(),
                        owner.getId(), pinned.getFirstName(), pinned.getLastName(),
                        pinned.getProfile().getAvatarUrl(), pinned.getProfile().getEmail(),
                        pinned.getLocation().getCity(), pinned.getLocation().getCountry(),
                        pinned.getProfile().getContactNumber(), pinned.getProfile().getBio());
        int pageSize = 3;
        Pageable pageable = Pageable.ofSize(pageSize);
        Page<ConnectionPinDto> pageResult =
                new PageImpl<>(Collections.singletonList(expectedConnectionPinDto), pageable, 1);

        when(connectionPinService.getConnectionPins(owner.getId()))
                .thenReturn(pageResult.getContent());

        ResultActions response = mockMvc
                .perform(get("/api/v1/connection-pins").contentType(MediaType.APPLICATION_JSON)
                        .param("ownerId", String.valueOf(owner.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(expectedConnectionPinDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].connectionPinId",
                        CoreMatchers.is(expectedConnectionPinDto.getConnectionPinId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].receiverId",
                        CoreMatchers.is(expectedConnectionPinDto.getReceiverId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].senderId",
                        CoreMatchers.is(expectedConnectionPinDto.getSenderId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].avatarUrl",
                        CoreMatchers.is(expectedConnectionPinDto.getAvatarUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email",
                        CoreMatchers.is(expectedConnectionPinDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].city",
                        CoreMatchers.is(expectedConnectionPinDto.getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].country",
                        CoreMatchers.is(expectedConnectionPinDto.getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].phoneNumber",
                        CoreMatchers.is(expectedConnectionPinDto.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].bio",
                        CoreMatchers.is(expectedConnectionPinDto.getBio())));

    }

    @Test
    public void ConnectionPinController_DeleteConnectionPin_ReturnNothing() throws Exception {
        doNothing().when(connectionPinService).deletePinnedConnection(connectionPin.getId());

        ResultActions response = mockMvc.perform(
                delete("/api/v1/connection-pins/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }
}


