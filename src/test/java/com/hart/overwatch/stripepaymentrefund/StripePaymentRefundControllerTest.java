package com.hart.overwatch.stripepaymentrefund;

import com.amazonaws.services.securityhub.model.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.request.UpdateStripePaymentRefundRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = StripePaymentRefundController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StripePaymentRefundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StripePaymentRefundService stripePaymentRefundService;

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

    private StripePaymentRefund stripePaymentRefund;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private User createReviewer() {
        Boolean loggedIn = true;
        User userEntity = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user.setId(2L);
        return userEntity;
    }

    private StripePaymentIntent createStripePaymentItent(User user, User reviewer) {
        StripePaymentIntent stripePaymentIntentEntity = new StripePaymentIntent();
        stripePaymentIntentEntity.setUser(user);
        stripePaymentIntentEntity.setReviewer(reviewer);
        stripePaymentIntentEntity.setAmount(10000L);
        stripePaymentIntentEntity.setCurrency("usd");
        stripePaymentIntentEntity.setDescription("description");
        stripePaymentIntentEntity.setClientSecret("client-secret");
        stripePaymentIntentEntity.setStatus(PaymentIntentStatus.PAID);
        stripePaymentIntentEntity.setApplicationFee(10000L);
        stripePaymentIntentEntity.setPaymentIntentId("stripe-paymentintent-id");

        stripePaymentIntentEntity.setId(1L);

        return stripePaymentIntentEntity;
    }

    private StripePaymentRefund createStripePaymentRefund(StripePaymentIntent stripePaymentIntent,
            User user) {
        StripePaymentRefund stripePaymentRefundEntity = new StripePaymentRefund();
        stripePaymentRefundEntity.setUser(user);
        stripePaymentRefundEntity.setStripePaymentIntent(stripePaymentIntent);
        stripePaymentRefundEntity.setAmount(10000L);
        stripePaymentRefundEntity.setReason("refund reason");
        stripePaymentRefundEntity.setCurrency("usd");
        stripePaymentRefundEntity.setRefundId("re_123");
        stripePaymentRefundEntity.setAdminNotes("admin notes");
        stripePaymentRefundEntity.setStatus(PaymentRefundStatus.PENDING);

        stripePaymentRefundEntity.setId(1L);


        return stripePaymentRefundEntity;
    }


    private StripePaymentRefundDto convertToDto(StripePaymentRefund stripePaymentRefund) {
        StripePaymentRefundDto dto = new StripePaymentRefundDto();
        dto.setId(stripePaymentRefund.getId());
        dto.setAmount(stripePaymentRefund.getAmount());
        dto.setUserId(stripePaymentRefund.getUser().getId());
        dto.setReason(stripePaymentRefund.getReason());
        dto.setCurrency(stripePaymentRefund.getCurrency());
        dto.setFullName(stripePaymentRefund.getUser().getFullName());
        dto.setAdminNotes(stripePaymentRefund.getAdminNotes());
        dto.setStatus(stripePaymentRefund.getStatus());
        dto.setStripePaymentIntentId(stripePaymentRefund.getStripePaymentIntent().getId());

        return dto;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);
        stripePaymentRefund = createStripePaymentRefund(stripePaymentIntent, user);
    }


    @Test
    public void StripePaymentRefundController_CreatePaymentRefund_ReturnCreateStripePaymentRefundResponse()
            throws Exception {
        CreateStripePaymentRefundRequest request = new CreateStripePaymentRefundRequest();
        request.setUserId(user.getId());
        request.setReason("refund reason");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());

        doNothing().when(stripePaymentRefundService).createPaymentRefund(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/payment-refunds").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void StripePaymentRefundController_GetPaymentRefunds_ReturnGetAllStripePaymentRefundResponse()
            throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        StripePaymentRefundDto stripePaymentRefundDto = convertToDto(stripePaymentRefund);
        Page<StripePaymentRefundDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentRefundDto), pageable, 1);
        PaginationDto<StripePaymentRefundDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(stripePaymentRefundService.getPaymentRefunds(user.getId(), page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/admin/%d/payment-refunds", user.getId()))
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

    @Test
    public void StripePaymentRefundController_UpdatePaymentRefund_ReturnNothing() throws Exception {
        Long paymentRefundId = stripePaymentRefund.getId();
        User admin = new User();
        admin.setId(3L);
        admin.setRole(Role.ADMIN);
        Long userId = admin.getId();

        UpdateStripePaymentRefundRequest request = new UpdateStripePaymentRefundRequest();
        request.setStatus("approve");
        request.setAdminNotes("admin notes");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());

        doNothing().when(stripePaymentRefundService).updatePaymentRefund(request, userId,
                paymentRefundId);

        ResultActions response = mockMvc.perform(
                patch(String.format("/api/v1/admin/%d/payment-refunds/%d", userId, paymentRefundId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }
}


