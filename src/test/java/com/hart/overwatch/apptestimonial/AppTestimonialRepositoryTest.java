package com.hart.overwatch.apptestimonial;

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
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_app_testimonial_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class AppTestimonialRepositoryTest {

    @Autowired
    private AppTestimonialRepository appTestimonialRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private AppTestimonial appTestimonial;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        profileRepository.save(profile);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private AppTestimonial createAppTestimonial(User user) {
        AppTestimonial appTestimonialEntity = new AppTestimonial();
        appTestimonialEntity.setDeveloperType("Frontend Developer");
        appTestimonialEntity.setContent("testimonial content");
        appTestimonialEntity.setUser(user);

        appTestimonialRepository.save(appTestimonialEntity);

        return appTestimonialEntity;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        appTestimonial = createAppTestimonial(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        appTestimonialRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void AppTestimonialRepository_GetAppTestimonials_ReturnPageOfAppTestimonialDto() {
        Pageable pageable = PageRequest.of(0, 1);

        Page<AppTestimonialDto> result = appTestimonialRepository.getAppTestimonials(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        AppTestimonialDto appTestimonialDto = result.getContent().get(0);

        Assertions.assertThat(appTestimonialDto.getDeveloperType())
                .isEqualTo(appTestimonial.getDeveloperType());
        Assertions.assertThat(appTestimonialDto.getContent())
                .isEqualTo(appTestimonial.getContent());
        Assertions.assertThat(appTestimonialDto.getAvatarUrl())
                .isEqualTo(appTestimonial.getUser().getProfile().getAvatarUrl());
        Assertions.assertThat(appTestimonialDto.getFirstName())
                .isEqualTo(appTestimonial.getUser().getFirstName());
    }

    @Test
    public void AppTestimonialRepository_GetAppTestimonialByUserId_ReturnAppTestimonial() {
        Long userId = user.getId();

        AppTestimonial result = appTestimonialRepository.getAppTestimonialByUserId(userId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).isEqualTo(appTestimonial.getContent());
        Assertions.assertThat(result.getDeveloperType())
                .isEqualTo(appTestimonial.getDeveloperType());
        Assertions.assertThat(result.getUser().getId()).isEqualTo(userId);
    }

    @Test
    public void AppTestimonialRepository_ExistsByUserId_ReturnBooleanTrue() {
        Long userId = user.getId();

        boolean exists = appTestimonialRepository.existsByUserId(userId);

        Assertions.assertThat(exists).isTrue();
    }
}


