package com.hart.overwatch.review;

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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.review.dto.ReviewDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.setting.Setting;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_review_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User author;
    private User reviewer;
    private Review review;

    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        author = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(author);
        userRepository.save(reviewer);
        System.out.println(String.format("REVIEWER ID IS: %d", reviewer.getId()));

        Boolean isEdited = true;
        Byte rating = 5;
        String reviewContent = "This is a review";
        review = new Review(author, reviewer, isEdited, rating, reviewContent);
        reviewRepository.save(review);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        reviewRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ReviewRepository_GetReviewByReviewerIdAndAuthorId_ReturnBoolean() {
        Boolean exists =
                reviewRepository.getReviewByReviewerIdAndAuthorId(reviewer.getId(), author.getId());
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void ReviewRepository_GetAllReviewsByReviewerId_ReturnPageOfReviewDto() {
        Boolean isEdited = true;
        Byte rating = 5;
        String reviewContent = "This is some review content";
        User author2 = new User("author2@mail.com", "Author", "Two", "Author Two", Role.USER, true,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(author2);

        review = new Review(author2, reviewer, isEdited, rating, reviewContent);
        reviewRepository.save(review);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewDto> reviewsByReviewer =
                reviewRepository.getAllReviewsByReviewerId(pageable, reviewer.getId());

        Assertions.assertThat(reviewsByReviewer).isNotNull();
        Assertions.assertThat(reviewsByReviewer.getContent().size()).isEqualTo(2);
    }

    @Test
    void ReviewRepository_GetAvgRatingByReviewerId_ReturnAvgRating() {
        Boolean isEdited = true;
        Byte rating = 2;
        String reviewContent = "This is some review content";
        User author2 = new User("author2@mail.com", "Author", "Two", "Author Two", Role.USER, true,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(author2);

        Review review2 = new Review(author2, reviewer, isEdited, rating, reviewContent);
        reviewRepository.save(review2);

        Float actualAvgRating = reviewRepository.getAvgRatingByReviewerId(reviewer.getId());
        Float expectedAverageRating = ((float) review.getRating() + review2.getRating()) / 2;

        Assertions.assertThat(actualAvgRating).isEqualTo(expectedAverageRating);
    }
}
