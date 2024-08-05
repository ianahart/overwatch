package com.hart.overwatch.setting;

import java.sql.Timestamp;
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
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.setting.dto.SettingDto;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_setting_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class SettingRepositoryTest {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Setting setting;

    @BeforeEach
    public void setUp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(user);

        setting = new Setting();
        setting.setCreatedAt(timestamp);
        setting.setUpdatedAt(timestamp);
        setting.setMfaEnabled(false);
        settingRepository.save(setting);

        user.setSetting(setting);
        setting.setUser(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        settingRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void SettingRepository_GetSettingById_ReturnSettingDto() {

        SettingDto settingDto = settingRepository.fetchSettingById(setting.getId());

        Assertions.assertThat(settingDto).isNotNull();
        Assertions.assertThat(settingDto.getId()).isEqualTo(setting.getId());
        Assertions.assertThat(setting.getMfaEnabled()).isEqualTo(setting.getMfaEnabled());

    }

    @Test
    public void SettingRepository_GetSettingById_ReturnSetting() {
        Setting returnedSetting = settingRepository.findById(setting.getId()).get();

        Assertions.assertThat(returnedSetting).isNotNull();
        Assertions.assertThat(returnedSetting.getId()).isEqualTo(setting.getId());
    }

    @Test
    public void SettingRepository_SaveSetting_ReturnSetting() {
        Setting returnedSetting = settingRepository.save(new Setting());

        Assertions.assertThat(returnedSetting).isNotNull();
        Assertions.assertThat(returnedSetting.getId()).isGreaterThan(1L);
    }
}

