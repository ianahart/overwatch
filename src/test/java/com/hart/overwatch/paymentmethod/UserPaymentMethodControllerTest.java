package com.hart.overwatch.paymentmethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.location.dto.LocationDto;
import com.hart.overwatch.location.request.CreateLocationRequest;
import com.hart.overwatch.paymentmethod.dto.UserPaymentMethodDto;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.stripe.exception.StripeException;
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
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserPaymentMethodController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserPaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPaymentMethodService userPaymentMethodService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private UserPaymentMethod userPaymentMethod;

    private UserPaymentMethod createUserPaymentMethod(User user) {
        UserPaymentMethod userPaymentMethod =
                new UserPaymentMethod(user, "New York City", "United States", "line 1", "line 2",
                        "03212", "John Doe", "visa", "card", 3, 2028, "dummy_stripe_customer_id");
        userPaymentMethod.setId(1L);
        userPaymentMethod.setUser(user);

        return userPaymentMethod;
    }

    private User createUser() {
        Boolean loggedIn = false;
        User user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user.setId(1L);

        return user;
    }


    @BeforeEach
    void setUp() {
        user = createUser();
        userPaymentMethod = createUserPaymentMethod(user);
        user.setUserPaymentMethods(List.of(userPaymentMethod));
    }

    @Test
    public void UserPaymentMethodController_CreateUserPaymentMethod_ReturnCreateUserPaymentMethodResponse()
            throws Exception {
        CreateUserPaymentMethodRequest request =
                new CreateUserPaymentMethodRequest("dummy_id", "New York City", "United States",
                        "line 1", "line 2", "03245", user.getFullName(), "visa", "card", 3, 28);
        doNothing().when(userPaymentMethodService).createUserPaymentMethod(user.getId(), request);

        ResultActions response = mockMvc.perform(
                post("/api/v1/users/1/payment-methods").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void UserPaymentMethodController_GetUserPaymentMethods_ReturnGetUserPaymentMethodResponse()
            throws Exception {
        UserPaymentMethodDto userPaymentMethodDto = new UserPaymentMethodDto(
                userPaymentMethod.getId(), "4242", userPaymentMethod.getDisplayBrand(), 12L, 2028L,
                userPaymentMethod.getName());


        when(userPaymentMethodService.getUserPaymentMethods(user.getId()))
                .thenReturn(userPaymentMethodDto);

        ResultActions response = mockMvc.perform(
                get("/api/v1/users/1/payment-methods").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(userPaymentMethodDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.last4",
                        CoreMatchers.is(userPaymentMethodDto.getLast4())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.displayBrand",
                        CoreMatchers.is(userPaymentMethodDto.getDisplayBrand())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.expMonth",
                        CoreMatchers.is(userPaymentMethodDto.getExpMonth().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.expYear",
                        CoreMatchers.is(userPaymentMethodDto.getExpYear().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name",
                        CoreMatchers.is(userPaymentMethodDto.getName())));
    }


}

