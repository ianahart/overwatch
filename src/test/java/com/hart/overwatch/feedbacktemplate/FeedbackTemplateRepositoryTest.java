package com.hart.overwatch.feedbacktemplate;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_feedback_template_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class FeedbackTemplateRepositoryTest {

    @Autowired
    private FeedbackTemplateRepository feedbackTemplateRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private List<FeedbackTemplate> feedbackTemplates = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }


    private List<FeedbackTemplate> createFeedbackTemplates(User user) {
        List<FeedbackTemplate> feedbackTemplateEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            FeedbackTemplate feedbackTemplateEntity = new FeedbackTemplate();
            feedbackTemplateEntity.setUser(user);
            feedbackTemplateEntity.setFeedback("feedback text");
            feedbackTemplateEntities.add(feedbackTemplateEntity);
        }


        feedbackTemplateRepository.saveAll(feedbackTemplateEntities);
        return feedbackTemplateEntities;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        feedbackTemplates = createFeedbackTemplates(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        feedbackTemplateRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void FeedbackTemplateRepository_CountFeedbackTemplatesByUserId_ReturnIntCount() {
        Long userId = user.getId();

        int count = feedbackTemplateRepository.countFeedbackTemplatesByUserId(userId);

        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    public void FeedbackTemplateRepository_GetFeedbackTemplates_ReturnListOfMinFeedbackTemplateDto() {
        Long userId = user.getId();
        List<MinFeedbackTemplateDto> feedbackTemplateDtos =
                feedbackTemplateRepository.getFeedbackTemplates(userId);

        Assertions.assertThat(feedbackTemplateDtos).hasSize(2);

        for (int i = 0; i < feedbackTemplateDtos.size(); i++) {
            Assertions.assertThat(feedbackTemplateDtos.get(i).getUserId())
                    .isEqualTo(feedbackTemplates.get(i).getUser().getId());
        }
    }

    @Test
    public void FeedbackTemplateRepository_GetFeedbackTemplate_ReturnFeedbackTemplateDto() {
        Long feedbackTemplateId = feedbackTemplates.get(0).getId();

        FeedbackTemplateDto feedbackTemplateDto =
                feedbackTemplateRepository.getFeedbackTemplate(feedbackTemplateId);

        Assertions.assertThat(feedbackTemplateDto).isNotNull();
        Assertions.assertThat(feedbackTemplateDto.getId()).isEqualTo(feedbackTemplateId);
    }
}


