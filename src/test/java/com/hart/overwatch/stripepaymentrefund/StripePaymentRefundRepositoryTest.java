package com.hart.overwatch.stripepaymentrefund;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentRepository;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_stripe_payment_refund_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class StripePaymentRefundRepositoryTest {

    @Autowired
    private StripePaymentRefundRepository stripePaymentRefundRepository;

    @Autowired
    private StripePaymentIntentRepository stripePaymentIntentRepository;

    @Autowired
    private UserRepository userRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private User reviewer;

    private StripePaymentIntent stripePaymentIntent;

    private StripePaymentRefund stripePaymentRefund;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private User createReviewer() {
        Boolean loggedIn = true;
        User userEntity = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
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

        stripePaymentIntentRepository.save(stripePaymentIntentEntity);

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

        stripePaymentRefundRepository.save(stripePaymentRefundEntity);

        return stripePaymentRefundEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);
        stripePaymentRefund = createStripePaymentRefund(stripePaymentIntent, user);
    }


    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        stripePaymentRefundRepository.deleteAll();
        stripePaymentIntentRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void StripePaymentRefundRepository_FindPaymentRefunds_ReturnPageOfStripePaymentRefundDto() {
        int page = 0, pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StripePaymentRefundDto> result =
                stripePaymentRefundRepository.findPaymentRefunds(pageable);

        Assertions.assertThat(result).isNotEmpty();
        List<StripePaymentRefundDto> stripePaymentRefundDtos = result.getContent();
        Assertions.assertThat(stripePaymentRefundDtos).hasSize(1);
        StripePaymentRefundDto stripePaymentRefundDto = stripePaymentRefundDtos.get(0);
        Assertions.assertThat(stripePaymentRefundDto.getId())
                .isEqualTo(stripePaymentRefund.getId());
        Assertions.assertThat(stripePaymentRefundDto.getAmount())
                .isEqualTo(stripePaymentRefund.getAmount());
        Assertions.assertThat(stripePaymentRefundDto.getUserId())
                .isEqualTo(stripePaymentRefund.getUser().getId());
        Assertions.assertThat(stripePaymentRefundDto.getStripePaymentIntentId())
                .isEqualTo(stripePaymentRefund.getStripePaymentIntent().getId());
        Assertions.assertThat(stripePaymentRefundDto.getReason())
                .isEqualTo(stripePaymentRefund.getReason());
        Assertions.assertThat(stripePaymentRefundDto.getCurrency())
                .isEqualTo(stripePaymentRefund.getCurrency());
        Assertions.assertThat(stripePaymentRefundDto.getFullName())
                .isEqualTo(stripePaymentRefund.getUser().getFullName());
        Assertions.assertThat(stripePaymentRefundDto.getAdminNotes())
                .isEqualTo(stripePaymentRefund.getAdminNotes());
        Assertions.assertThat(stripePaymentRefundDto.getStatus())
                .isEqualTo(stripePaymentRefund.getStatus());
    }
}


