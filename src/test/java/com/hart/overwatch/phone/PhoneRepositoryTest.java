package com.hart.overwatch.phone;

import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.hart.overwatch.phone.dto.PhoneDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_phone_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class PhoneRepositoryTest {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Phone phone1;



    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        phone1 = new Phone("4444444444", true, user);

        user.setPhones(Arrays.asList(phone1));

        userRepository.save(user);
        phoneRepository.save(phone1);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        phoneRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void PhoneRepository_DeletePhoneById_ReturnNothing() {
        Long phoneId = phone1.getId();
        phoneRepository.deleteByPhoneId(phoneId);

        entityManager.flush();
        entityManager.clear();

        Optional<Phone> deletedPhone = phoneRepository.findById(phoneId);

        Assertions.assertThat(deletedPhone).isEmpty();
    }

    @Test
    public void PhoneRepository_ExistsByUserId_ReturnBooleanTrue() {
        boolean exists = phoneRepository.existsByUserId(user.getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void PhoneRepository_GetLatestPhoneByUserId_ReturnPhoneDto() {
        PhoneDto phoneDto = phoneRepository.getLatestPhoneByUserId(user.getId());

        Assertions.assertThat(phoneDto).isNotNull();
    }

}


