package com.hart.overwatch.suggestion;

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
import com.hart.overwatch.suggestion.dto.SuggestionDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_suggestion_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class SuggestionRepositoryTest {

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private List<Suggestion> suggestions = new ArrayList<>();

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileRepository.save(profileEntity);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private List<Suggestion> createSuggestions(User user, int numOfSuggestions) {
        List<Suggestion> suggestionEntities = new ArrayList<>();
        for (int i = 0; i < numOfSuggestions; i++) {
            Suggestion suggestionEntity = new Suggestion();
            suggestionEntity.setUser(user);
            suggestionEntity.setTitle("title");
            suggestionEntity.setContact("contact");
            suggestionEntity.setFileUrl("https://www.s3.com");
            suggestionEntity.setFileName("filename");
            suggestionEntity.setDescription("description");
            suggestionEntity.setFeedbackType(FeedbackType.FEATURE_REQUEST);
            suggestionEntity.setPriorityLevel(PriorityLevel.LOW);
            suggestionEntity.setFeedbackStatus(FeedbackStatus.PENDING);
            suggestionEntities.add(suggestionEntity);
        }
        suggestionRepository.saveAll(suggestionEntities);

        return suggestionEntities;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        int numOfSuggestions = 3;
        suggestions = createSuggestions(user, numOfSuggestions);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        suggestionRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void SuggestionRepository_GetAllSuggestions_ReturnPageOfSuggestionDto() {
        int page = 0, pageSize = 3;
        FeedbackStatus feedbackStatus = FeedbackStatus.PENDING;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<SuggestionDto> result =
                suggestionRepository.getAllSuggestions(pageable, feedbackStatus);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(suggestions.size());

        List<SuggestionDto> dtos = result.getContent();

        for (int i = 0; i < dtos.size(); i++) {
            Assertions.assertThat(dtos.get(i).getId()).isEqualTo(suggestions.get(i).getId());
            Assertions.assertThat(dtos.get(i).getTitle()).isEqualTo(suggestions.get(i).getTitle());
            Assertions.assertThat(dtos.get(i).getContact())
                    .isEqualTo(suggestions.get(i).getContact());
            Assertions.assertThat(dtos.get(i).getFileUrl())
                    .isEqualTo(suggestions.get(i).getFileUrl());
            Assertions.assertThat(dtos.get(i).getFullName())
                    .isEqualTo(suggestions.get(i).getUser().getFullName());
            Assertions.assertThat(dtos.get(i).getAvatarUrl())
                    .isEqualTo(suggestions.get(i).getUser().getProfile().getAvatarUrl());
            Assertions.assertThat(dtos.get(i).getDescription())
                    .isEqualTo(suggestions.get(i).getDescription());
            Assertions.assertThat(dtos.get(i).getFeedbackType())
                    .isEqualTo(suggestions.get(i).getFeedbackType());
            Assertions.assertThat(dtos.get(i).getPriorityLevel())
                    .isEqualTo(suggestions.get(i).getPriorityLevel());
            Assertions.assertThat(dtos.get(i).getFeedbackStatus())
                    .isEqualTo(suggestions.get(i).getFeedbackStatus());
        }


    }

}


