package com.hart.overwatch.reviewfeedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.repository.RepositoryRepository;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackRatingsDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_review_feedback_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ReviewFeedbackRepositoryTest {

    @Autowired
    private ReviewFeedbackRepository reviewFeedbackRepository;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User owner;

    private User reviewer;

    private Repository repository;

    private ReviewFeedback reviewFeedback;

    private User createOwner() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/1");
        profileRepository.save(profile);

        owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        userRepository.save(owner);

        return owner;
    }

    private User createReviewer() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/2");
        profileRepository.save(profile);

        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        userRepository.save(reviewer);

        return reviewer;
    }

    private Repository createRepository(User owner, User reviewer) {
        Repository repository = new Repository();
        LocalDateTime dateTime = LocalDateTime.now();

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);
        repository.setReviewStartTime(dateTime);
        repository.setReviewEndTime(dateTime);

        repositoryRepository.save(repository);

        return repository;
    }

    private ReviewFeedback createReviewFeedback(User owner, User reviewer, Repository repository) {
        ReviewFeedback reviewFeedback = new ReviewFeedback();
        reviewFeedback.setClarity(1);
        reviewFeedback.setThoroughness(1);
        reviewFeedback.setResponseTime(1);
        reviewFeedback.setHelpfulness(1);
        reviewFeedback.setOwner(owner);
        reviewFeedback.setReviewer(reviewer);
        reviewFeedback.setRepository(repository);

        reviewFeedbackRepository.save(reviewFeedback);

        return reviewFeedback;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewFeedback = createReviewFeedback(owner, reviewer, repository);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        reviewFeedbackRepository.deleteAll();
        repositoryRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ReviewFeedbackRepository_GetReviewFeedbackRatings_ReturnListOfReviewFeedbackRatingsDto() {
        Long reviewerId = reviewer.getId();
        List<ReviewFeedbackRatingsDto> reviewFeedbackRatings =
                reviewFeedbackRepository.getReviewFeedbackRatings(reviewerId);

        Assertions.assertThat(reviewFeedbackRatings).isNotNull();
        Assertions.assertThat(reviewFeedbackRatings.size()).isEqualTo(1L);

        ReviewFeedbackRatingsDto reviewFeedbackRating = reviewFeedbackRatings.getFirst();

        Assertions.assertThat(reviewFeedbackRating.getClarity())
                .isEqualTo(reviewFeedback.getClarity());
        Assertions.assertThat(reviewFeedbackRating.getResponseTime())
                .isEqualTo(reviewFeedback.getResponseTime());
        Assertions.assertThat(reviewFeedbackRating.getThoroughness())
                .isEqualTo(reviewFeedback.getThoroughness());
        Assertions.assertThat(reviewFeedbackRating.getHelpfulness())
                .isEqualTo(reviewFeedback.getHelpfulness());
    }

    @Test
    public void ReviewFeedbackRepository_GetSingleReviewFeedback_ReturnReviewFeedbackDto() {
        Long ownerId = owner.getId();
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();

        ReviewFeedbackDto reviewFeedbackDto =
                reviewFeedbackRepository.getSingleReviewFeedback(ownerId, reviewerId, repositoryId);

        Assertions.assertThat(reviewFeedbackDto).isNotNull();
        Assertions.assertThat(reviewFeedbackDto.getOwnerId())
                .isEqualTo(reviewFeedback.getOwner().getId());
        Assertions.assertThat(reviewFeedbackDto.getReviewerId())
                .isEqualTo(reviewFeedback.getReviewer().getId());
        Assertions.assertThat(reviewFeedbackDto.getRepositoryId())
                .isEqualTo(reviewFeedback.getRepository().getId());
        Assertions.assertThat(reviewFeedbackDto.getClarity())
                .isEqualTo(reviewFeedback.getClarity());
        Assertions.assertThat(reviewFeedbackDto.getResponseTime())
                .isEqualTo(reviewFeedback.getResponseTime());
        Assertions.assertThat(reviewFeedbackDto.getThoroughness())
                .isEqualTo(reviewFeedback.getThoroughness());
        Assertions.assertThat(reviewFeedbackDto.getHelpfulness())
                .isEqualTo(reviewFeedback.getHelpfulness());
    }
}

