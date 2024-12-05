package com.hart.overwatch.stripepaymentintent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StripePaymentIntentServiceTest {

    @InjectMocks
    private StripePaymentIntentService stripePaymentIntentService;

    @Mock
    private StripePaymentIntentRepository stripePaymentIntentRepository;

    @Mock
    private EmailQueueService EmailQueueService;

    @Mock
    private CsvFileService csvFileService;

    @Mock
    private PaginationService paginationService;

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

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);

    }

    @Test
    public void StripePaymentIntentService_GetStripePaymentIntentById_ThrowNotFoundException() {
        Long nonExistentStripePaymentIntentId = 999L;

        when(stripePaymentIntentRepository.findById(nonExistentStripePaymentIntentId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            stripePaymentIntentService.getStripePaymentIntentById(nonExistentStripePaymentIntentId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Could not find stripe payment intent with the id %d",
                        nonExistentStripePaymentIntentId));
    }

    @Test
    public void StripePaymentIntentService_GetStripePaymentIntentById_ReturnStripePaymentIntent() {
        Long stripePaymentIntentId = stripePaymentIntent.getId();
        when(stripePaymentIntentRepository.findById(stripePaymentIntentId))
                .thenReturn(Optional.of(stripePaymentIntent));

        StripePaymentIntent returnedStripePaymentIntent =
                stripePaymentIntentService.getStripePaymentIntentById(stripePaymentIntentId);

        Assertions.assertThatNoException();
        Assertions.assertThat(returnedStripePaymentIntent.getId())
                .isEqualTo(stripePaymentIntent.getId());
        Assertions.assertThat(returnedStripePaymentIntent.getAmount())
                .isEqualTo(stripePaymentIntent.getAmount());
        Assertions.assertThat(returnedStripePaymentIntent.getApplicationFee())
                .isEqualTo(stripePaymentIntent.getApplicationFee());
        Assertions.assertThat(returnedStripePaymentIntent.getCurrency())
                .isEqualTo(stripePaymentIntent.getCurrency());
        Assertions.assertThat(returnedStripePaymentIntent.getDescription())
                .isEqualTo(stripePaymentIntent.getDescription());
        Assertions.assertThat(returnedStripePaymentIntent.getClientSecret())
                .isEqualTo(stripePaymentIntent.getClientSecret());
        Assertions.assertThat(returnedStripePaymentIntent.getPaymentIntentId())
                .isEqualTo(stripePaymentIntent.getPaymentIntentId());
        Assertions.assertThat(returnedStripePaymentIntent.getStatus())
                .isEqualTo(stripePaymentIntent.getStatus());

    }

}
