package com.hart.overwatch.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.phone.dto.PhoneDto;
import com.hart.overwatch.phone.request.CreatePhoneRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import java.sql.Timestamp;
import java.util.Arrays;

@ActiveProfiles("test")
@WebMvcTest(controllers = PhoneController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneService phoneService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Phone phone;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        Boolean isVerified = false;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        phone = new Phone("4444444444", isVerified, user);

        user.setPhones(Arrays.asList(phone));

    }

    @Test
    public void PhoneController_CreatePhone_ReturnCreatePhoneResponse() throws Exception {
        String phoneNumber = "4444444444";
        CreatePhoneRequest request = new CreatePhoneRequest(user.getId(), phoneNumber);

        doNothing().when(phoneService).createPhone(request.getUserId(), request.getPhoneNumber());

        ResultActions response =
                mockMvc.perform(post("/api/v1/phones").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void PhoneController_GetPhone_ReturnGetPhoneResponse() throws Exception {
        phone.setId(1L);
        user.setId(1L);
        phone.setUser(user);

        PhoneDto phoneDto = new PhoneDto(phone.getId(), new Timestamp(System.currentTimeMillis()),
                true, "4444444444");

        when(phoneService.getPhone(user.getId())).thenReturn(phoneDto);

        ResultActions response = mockMvc.perform(
                get("/api/v1/phones").contentType(MediaType.APPLICATION_JSON).param("userId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(phone.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.isVerified", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber",
                        CoreMatchers.is(phone.getPhoneNumber())));

    }

    @Test
    public void PhoneController_DeletePhone_ReturnDeletePhoneResponse() throws Exception {
        phone.setId(1L);
        user.setId(1L);
        phone.setUser(user);

        doNothing().when(phoneService).deletePhone(phone.getId());

        ResultActions response =
                mockMvc.perform(delete("/api/v1/phones/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

}


