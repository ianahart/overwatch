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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.hart.overwatch.setting.Setting;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ReviewRepositoryTest {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository, UserRepository userRepository,
            ProfileRepository profileRepository, JdbcTemplate jdbcTemplate) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private User author;

    private User reviewer;

    private Review review;

    @BeforeEach
    public void setUp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());



        Boolean loggedIn = true;
        author = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        this.userRepository.save(author);
        this.userRepository.save(reviewer);

        Boolean isEdited = true;
        Byte rating = 5;
        String reviewContent = "This is a review";
        review = new Review(author, reviewer, isEdited, rating, reviewContent);
        this.reviewRepository.save(review);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing  down the test data....");
        this.reviewRepository.deleteAll();
        this.profileRepository.deleteAll();
        this.userRepository.deleteAll();
        resetSequences();

    }

    private void resetSequences() {
        System.out.println("Resetting sequences");
        jdbcTemplate.execute("ALTER SEQUENCE review_sequence RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE profile_sequence RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE _user_sequence RESTART WITH 1");
    }

    @Test
    public void ReviewRepository_GetReviewByReviewerIdAndAuthorId_ReturnBoolean() {


        Boolean exists = this.reviewRepository.getReviewByReviewerIdAndAuthorId(reviewer.getId(),
                author.getId());
        Assertions.assertThat(exists).isTrue();

    }

}
