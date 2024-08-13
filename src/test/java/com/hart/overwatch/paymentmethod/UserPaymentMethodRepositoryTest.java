package com.hart.overwatch.paymentmethod;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_payment_method_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserPaymentMethodRepositoryTest {

    @Autowired
    private UserPaymentMethodRepository userPaymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private UserPaymentMethod userPaymentMethod;


    private UserPaymentMethod createUserPaymentMethod(User user) {
        UserPaymentMethod userPaymentMethod =
                new UserPaymentMethod(user, "New York City", "United States", "line 1", "line 2",
                        "03212", "John Doe", "visa", "card", 3, 26, "dummy_stripe_customer_id");
        userPaymentMethodRepository.save(userPaymentMethod);

        return userPaymentMethod;
    }

    private User createUser() {
        Boolean loggedIn = false;
        User user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(user);

        return user;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        userPaymentMethod = createUserPaymentMethod(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userPaymentMethodRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void UserPaymentMethodRepository_GetUserPaymentMethodByUserId_ReturnUserPaymentMethod() {
        UserPaymentMethod returnedUserPaymentMethod =
                userPaymentMethodRepository.getUserPaymentMethodByUserId(user.getId());

        Assertions.assertThat(returnedUserPaymentMethod).isNotNull();
        Assertions.assertThat(returnedUserPaymentMethod.getUser().getId()).isEqualTo(user.getId());
    }

}


