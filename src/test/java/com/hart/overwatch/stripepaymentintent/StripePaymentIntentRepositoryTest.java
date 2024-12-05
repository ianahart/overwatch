package com.hart.overwatch.stripepaymentintent;

import java.util.List;
import java.util.ArrayList;
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
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_stripe_payment_intent_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class StripePaymentIntentRepositoryTest {

    @Autowired
    private StripePaymentIntentRepository stripePaymentIntentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private User reviewer;

    private StripePaymentIntent stripePaymentIntent;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-01");
        profileRepository.save(profileEntity);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private User createReviewer() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-02");
        profileRepository.save(profileEntity);
        User userEntity = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
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

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);

    }


    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        stripePaymentIntentRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void StripePaymentIntentRepository_GetStripePaymentItentsBySearch() {
        int page = 0, pageSize = 3;
        String search = "John Doe";
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<FullStripePaymentIntentDto> result =
                stripePaymentIntentRepository.getStripePaymentIntentsBySearch(pageable, search);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        FullStripePaymentIntentDto dto = result.getContent().get(0);
        Assertions.assertThat(dto.getId()).isEqualTo(stripePaymentIntent.getId());
        Assertions.assertThat(dto.getAmount()).isEqualTo(stripePaymentIntent.getAmount());
        Assertions.assertThat(dto.getUserId()).isEqualTo(stripePaymentIntent.getUser().getId());
        Assertions.assertThat(dto.getReviewerId())
                .isEqualTo(stripePaymentIntent.getReviewer().getId());
        Assertions.assertThat(dto.getApplicationFee())
                .isEqualTo(stripePaymentIntent.getApplicationFee());
        Assertions.assertThat(dto.getCurrency()).isEqualTo(stripePaymentIntent.getCurrency());
        Assertions.assertThat(dto.getUserEmail())
                .isEqualTo(stripePaymentIntent.getUser().getEmail());
        Assertions.assertThat(dto.getDescription()).isEqualTo(stripePaymentIntent.getDescription());
        Assertions.assertThat(dto.getUserFullName())
                .isEqualTo(stripePaymentIntent.getUser().getFullName());
        Assertions.assertThat(dto.getReviewerEmail())
                .isEqualTo(stripePaymentIntent.getReviewer().getEmail());
        Assertions.assertThat(dto.getReviewerFullName())
                .isEqualTo(stripePaymentIntent.getReviewer().getFullName());
        Assertions.assertThat(dto.getStatus()).isEqualTo(stripePaymentIntent.getStatus());
    }


}


