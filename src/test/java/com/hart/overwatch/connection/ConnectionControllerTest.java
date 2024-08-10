package com.hart.overwatch.connection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.connection.request.CreateConnectionRequest;
import com.hart.overwatch.location.dto.LocationDto;
import com.hart.overwatch.location.request.CreateLocationRequest;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = ConnectionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void ConnectionController_CreateConnection_ReturnCreateConnectionResponse()
            throws Exception {

        CreateConnectionRequest request =
                new CreateConnectionRequest(receiver.getId(), sender.getId());

        doNothing().when(connectionService).createConnection(request.getReceiverId(),
                request.getSenderId());

        ResultActions response =
                mockMvc.perform(post("/api/v1/connections").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    // @Test
    // public void LocationController_GetAllLocations_ReturnGetLocationAutoCompleteResponse ()
    // throws Exception {
    // when(locationService.getLocationAutoComplete("42 main")).thenReturn("42 main street");
    //
    // ResultActions response =
    // mockMvc.perform(get("/api/v1/locations/autocomplete").contentType(MediaType.APPLICATION_JSON).param("text",
    // "42 main"));
    //
    // response.andExpect(MockMvcResultMatchers.status().isOk())
    // .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("42 main street")));
    // }
    //



}

