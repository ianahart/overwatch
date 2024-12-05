package com.hart.overwatch.stripepaymentrefund;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.csv.CsvFileService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentService;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.request.UpdateStripePaymentRefundRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StripePaymentRefundServiceTest {

    @InjectMocks
    private StripePaymentRefundService stripePaymentRefundService;

    @Mock
    private StripePaymentRefundRepository stripePaymentRefundRepository;

    @Mock
    private StripePaymentIntentService stripePaymentIntentService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    @Captor
    private ArgumentCaptor<StripePaymentRefund> refundCaptor;

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
    public void StripePaymentRefundService_CreatePaymentRefund_ThrowForbiddenException() {
        CreateStripePaymentRefundRequest request = new CreateStripePaymentRefundRequest();
        request.setUserId(999L);
        request.setReason("refund reason");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        Assertions.assertThatThrownBy(() -> {
            stripePaymentRefundService.createPaymentRefund(request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot ask for a payment refund when the payment is not yours");
    }

    @Test
    public void StripePaymentRefundService_CreatePaymentRefund_ThrowBadRequestException() {
        CreateStripePaymentRefundRequest request = new CreateStripePaymentRefundRequest();
        request.setUserId(user.getId());
        request.setReason("refund reason");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(stripePaymentRefundRepository.findPaymentRefundByUserIdAndStripePaymentIntentId(
                request.getUserId(), request.getStripePaymentIntentId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            stripePaymentRefundService.createPaymentRefund(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already asked for a refund for this payment");
    }

    @Test
    public void StripePaymentRefundService_CreatePaymentRefund_ReturnNothing() {
        CreateStripePaymentRefundRequest request = new CreateStripePaymentRefundRequest();
        request.setUserId(user.getId());
        request.setReason("refund reason");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(stripePaymentRefundRepository.findPaymentRefundByUserIdAndStripePaymentIntentId(
                request.getUserId(), request.getStripePaymentIntentId())).thenReturn(false);

        when(stripePaymentIntentService
                .getStripePaymentIntentById(request.getStripePaymentIntentId()))
                        .thenReturn(stripePaymentIntent);
        StripePaymentRefund newStripePaymentRefund = new StripePaymentRefund();
        newStripePaymentRefund.setStripePaymentIntent(stripePaymentIntent);
        newStripePaymentRefund.setUser(user);
        newStripePaymentRefund.setAmount(10000L);
        newStripePaymentRefund.setCurrency("usd");
        newStripePaymentRefund.setRefundId("re_123");
        newStripePaymentRefund.setStatus(PaymentRefundStatus.PENDING);

        when(stripePaymentRefundRepository.save(any(StripePaymentRefund.class)))
                .thenReturn(newStripePaymentRefund);
        doNothing().when(stripePaymentIntentService).updateStatus(PaymentIntentStatus.PENDING,
                request.getStripePaymentIntentId());

        stripePaymentRefundService.createPaymentRefund(request);

        verify(stripePaymentIntentService, times(1))
                .getStripePaymentIntentById(request.getStripePaymentIntentId());
        verify(stripePaymentRefundRepository, times(1)).save(any(StripePaymentRefund.class));
    }

    @Test
    public void StripePaymentRefundService_GetPaymentRefunds_ThrowForbiddenException() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Long userId = user.getId();
        when(userService.getUserById(user.getId())).thenReturn(user);

        Assertions.assertThatThrownBy(() -> {
            stripePaymentRefundService.getPaymentRefunds(userId, page, pageSize, direction);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have priveleges to access this route");
    }

    @Test
    public void StripePaymentRefundService_GetPaymentRefunds_ReturnPaginationOfStripePaymentRefundDto() {
        User admin = new User("admin@mail.com", "Admin", "User", "Admin User", Role.ADMIN, true,
                new Profile(), "password123", new Setting());
        admin.setId(3L);
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        StripePaymentRefundDto expectedStripePaymentRefundDto = convertToDto(stripePaymentRefund);
        Page<StripePaymentRefundDto> pageResult = new PageImpl<>(
                Collections.singletonList(expectedStripePaymentRefundDto), pageable, 1);
        PaginationDto<StripePaymentRefundDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(userService.getUserById(admin.getId())).thenReturn(admin);
        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(stripePaymentRefundRepository.findPaymentRefunds(pageable)).thenReturn(pageResult);

        PaginationDto<StripePaymentRefundDto> actualPaginationDto = stripePaymentRefundService
                .getPaymentRefunds(admin.getId(), page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        StripePaymentRefundDto actualStripePaymentRefundDto = actualPaginationDto.getItems().get(0);

        Assertions.assertThat(actualStripePaymentRefundDto.getId())
                .isEqualTo(expectedStripePaymentRefundDto.getId());
        Assertions.assertThat(actualStripePaymentRefundDto.getAmount())
                .isEqualTo(expectedStripePaymentRefundDto.getAmount());
        Assertions.assertThat(actualStripePaymentRefundDto.getUserId())
                .isEqualTo(expectedStripePaymentRefundDto.getUserId());
        Assertions.assertThat(actualStripePaymentRefundDto.getStripePaymentIntentId())
                .isEqualTo(expectedStripePaymentRefundDto.getStripePaymentIntentId());
        Assertions.assertThat(actualStripePaymentRefundDto.getReason())
                .isEqualTo(expectedStripePaymentRefundDto.getReason());
        Assertions.assertThat(actualStripePaymentRefundDto.getCurrency())
                .isEqualTo(expectedStripePaymentRefundDto.getCurrency());
        Assertions.assertThat(actualStripePaymentRefundDto.getFullName())
                .isEqualTo(expectedStripePaymentRefundDto.getFullName());
        Assertions.assertThat(actualStripePaymentRefundDto.getAdminNotes())
                .isEqualTo(expectedStripePaymentRefundDto.getAdminNotes());
        Assertions.assertThat(actualStripePaymentRefundDto.getStatus())
                .isEqualTo(expectedStripePaymentRefundDto.getStatus());

    }

    @Test
    public void StripePaymentRefundService_UpdatePaymentRefund_ReturnNothing_RejectRefund() {
        Long userId = user.getId();
        Long paymentRefundId = stripePaymentRefund.getId();
        UpdateStripePaymentRefundRequest request = new UpdateStripePaymentRefundRequest();
        request.setStatus("reject");
        request.setAdminNotes("admin notes");
        request.setStripePaymentIntentId(stripePaymentIntent.getId());
        when(stripePaymentIntentService
                .getStripePaymentIntentById(request.getStripePaymentIntentId()))
                        .thenReturn(stripePaymentIntent);
        when(stripePaymentRefundRepository.findById(stripePaymentRefund.getId()))
                .thenReturn(Optional.of(stripePaymentRefund));

        stripePaymentRefundService.updatePaymentRefund(request, userId, paymentRefundId);

        verify(stripePaymentRefundRepository).save(refundCaptor.capture());
        StripePaymentRefund savedRefund = refundCaptor.getValue();
        Assertions.assertThat(savedRefund.getStatus()).isEqualTo(PaymentRefundStatus.REJECTED);
        Assertions.assertThat(savedRefund.getAdminNotes()).isEqualTo(request.getAdminNotes());
    }
}

