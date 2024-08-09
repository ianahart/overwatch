package com.hart.overwatch.location;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.location.dto.LocationDto;
import com.hart.overwatch.location.request.CreateLocationRequest;
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
@WebMvcTest(controllers = LocationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Location location;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        location = new Location(user, "United States", "44 Main Street", "", "Chicago", "IL",
                "34122", "4444444444");

        user.setId(1L);
        location.setId(1L);
        user.setLocation(location);

    }

    @Test
    public void LocationController_GetAllLocations_ReturnGetLocationAutoCompleteResponse () throws Exception {
        when(locationService.getLocationAutoComplete("42 main")).thenReturn("42 main street");

        ResultActions response = mockMvc.perform(get("/api/v1/locations/autocomplete").contentType(MediaType.APPLICATION_JSON).param("text", "42 main"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("42 main street")));
    }

    @Test
    public void LocationController_CreateOrUpdateLocation_ReturnCreateLocationResponse()
            throws Exception {
        Long userId = user.getId();
        CreateLocationRequest request = new CreateLocationRequest("12 main street", "",
                "New York City", "United States", "4444444444", "NY", "11111");

        doNothing().when(locationService).createOrUpdateLocation(userId, request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/users/1/locations").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void LocationController_GetLocation_ReturnGetFullLocationResponse() throws Exception {
        Long userId = user.getId();
        LocationDto locationDto = new LocationDto(location.getAddress(), location.getAddressTwo(),
                location.getCity(), location.getCountry(), location.getPhoneNumber(),
                location.getState(), location.getZipCode());

        when(locationService.getFullLocationByUserId(userId)).thenReturn(locationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/users/1/locations")
                .contentType(MediaType.APPLICATION_JSON).param("userId", "1"));


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address",
                        CoreMatchers.is(locationDto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.addressTwo",
                        CoreMatchers.is(locationDto.getAddressTwo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.city",
                        CoreMatchers.is(locationDto.getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.country",
                        CoreMatchers.is(locationDto.getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber",
                        CoreMatchers.is(locationDto.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.state",
                        CoreMatchers.is(locationDto.getState())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.zipCode",
                        CoreMatchers.is(locationDto.getZipCode())));

    }



}
