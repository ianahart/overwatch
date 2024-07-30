package com.hart.overwatch.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PaginationService paginationService;

    @Mock
    private UserService userService;

    private User author;

    private User reviewer;

    private Review review;


    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        Boolean isEdited = true;
        Byte rating = 5;
        String reviewContent = "This is a review";
        author = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        review = new Review(author, reviewer, isEdited, rating, reviewContent);

        author.setId(1L);
        reviewer.setId(2L);
        review.setId(1L);
    }


    @Test
    public void ReviewService_GetAvgReviewRating_ReturnAvgRating() {
        Float expectedAverageRating = 5F;
        when(reviewRepository.getAvgRatingByReviewerId(reviewer.getId()))
                .thenReturn(expectedAverageRating);

        Float actualAvgRating = reviewService.getAvgReviewRating(reviewer.getId());
        Assertions.assertThat(actualAvgRating).isEqualTo(expectedAverageRating);
    }

    @Test
    public void ReviewService_GetAvgReviewRating_Throws_Bad_Request_Exception() {
        Long nonExistentReviewerId = 3L;
        when(reviewRepository.getAvgRatingByReviewerId(nonExistentReviewerId))
                .thenThrow(new DataAccessException("Database error") {});

        Assertions.assertThatThrownBy(() -> reviewService.getAvgReviewRating(nonExistentReviewerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Could not get avg review rating");
    }

}

