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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.csv.CsvFileService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentService;
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

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);
        stripePaymentRefund = createStripePaymentRefund(stripePaymentIntent, user);
    }


}

