package com.hart.overwatch.stripepaymentintent;


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = StripePaymentIntentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StripePaymentIntentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StripePaymentIntentService stripePaymentIntentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private User reviewer;

    private StripePaymentIntent stripePaymentIntent;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-01");
        profileEntity.setId(1L);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private User createReviewer() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-02");
        profileEntity.setId(2L);
        User userEntity = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(2L);
        return userEntity;
    }

    private StripePaymentIntent createStripePaymentItent(User user, User reviewer) {
        StripePaymentIntent stripePaymentIntentEntity = new StripePaymentIntent();
        stripePaymentIntentEntity.setId(1L);
        stripePaymentIntentEntity.setUser(user);
        stripePaymentIntentEntity.setReviewer(reviewer);
        stripePaymentIntentEntity.setAmount(10000L);
        stripePaymentIntentEntity.setCurrency("usd");
        stripePaymentIntentEntity.setDescription("description");
        stripePaymentIntentEntity.setClientSecret("client-secret");
        stripePaymentIntentEntity.setStatus(PaymentIntentStatus.PAID);
        stripePaymentIntentEntity.setApplicationFee(10000L);
        stripePaymentIntentEntity.setPaymentIntentId("stripe-paymentintent-id");


        return stripePaymentIntentEntity;
    }

    private StripePaymentIntentDto convertToMinDto(StripePaymentIntent stripePaymentIntent) {
        StripePaymentIntentDto dto = new StripePaymentIntentDto();
        dto.setId(stripePaymentIntent.getId());
        dto.setAmount(stripePaymentIntent.getAmount());
        dto.setCurrency(stripePaymentIntent.getCurrency());
        dto.setFullName(stripePaymentIntent.getUser().getFullName());
        dto.setAvatarUrl(stripePaymentIntent.getUser().getProfile().getAvatarUrl());
        dto.setReviewerId(stripePaymentIntent.getReviewer().getId());
        dto.setStatus(stripePaymentIntent.getStatus());

        return dto;
    }

    private FullStripePaymentIntentDto convertToFullDto(StripePaymentIntent stripePaymentIntent) {
        FullStripePaymentIntentDto dto = new FullStripePaymentIntentDto();
        dto.setId(stripePaymentIntent.getId());
        dto.setAmount(stripePaymentIntent.getAmount());
        dto.setUserId(stripePaymentIntent.getUser().getId());
        dto.setCurrency(stripePaymentIntent.getCurrency());
        dto.setReviewerId(stripePaymentIntent.getReviewer().getId());
        dto.setUserEmail(stripePaymentIntent.getUser().getEmail());
        dto.setDescription(stripePaymentIntent.getDescription());
        dto.setUserFullName(stripePaymentIntent.getUser().getFullName());
        dto.setStatus(stripePaymentIntent.getStatus());
        dto.setApplicationFee(stripePaymentIntent.getApplicationFee());
        dto.setReviewerEmail(stripePaymentIntent.getReviewer().getEmail());
        dto.setReviewerFullName(stripePaymentIntent.getReviewer().getFullName());

        return dto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);

    }

    @Test
    public void StripePaymentIntentController_GetUserStripePaymentIntents_ReturnGetAllStripePaymentIntentResponse()
            throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        StripePaymentIntentDto stripePaymentIntentDto = convertToMinDto(stripePaymentIntent);
        Page<StripePaymentIntentDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentIntentDto), pageable, 1);
        PaginationDto<StripePaymentIntentDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(stripePaymentIntentService.getUserStripePaymentIntents(user.getId(), page, pageSize,
                direction)).thenReturn(expectedPaginationDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/users/%d/payment-intents", user.getId()))
                        .param("page", "0").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));

    }

}


