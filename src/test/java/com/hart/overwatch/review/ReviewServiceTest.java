package com.hart.overwatch.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.review.dto.MinReviewDto;
import com.hart.overwatch.review.dto.ReviewDto;
import com.hart.overwatch.review.request.CreateReviewRequest;
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
        review = new Review(author, reviewer, isEdited, rating,
                Jsoup.clean(reviewContent, Safelist.none()));

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

    @Test
    public void ReviewService_CreateReview_ReturnNothing() {
        Byte rating = 5;
        CreateReviewRequest request =
                new CreateReviewRequest(author.getId(), reviewer.getId(), rating, "Some content");

        when(reviewRepository.getReviewByReviewerIdAndAuthorId(request.getReviewerId(),
                request.getAuthorId())).thenReturn(false);

        when(userService.getUserById(request.getAuthorId())).thenReturn(author);
        when(userService.getUserById(request.getReviewerId())).thenReturn(reviewer);

        reviewService.createReview(request);

        verify(reviewRepository).save(argThat(savedReview -> savedReview.getAuthor().equals(author)
                && savedReview.getReviewer().equals(reviewer)
                && savedReview.getRating().equals(request.getRating()) && savedReview.getReview()
                        .equals(Jsoup.clean(request.getReview(), Safelist.none()))));
    }

    @Test
    public void ReviewService_CreateReview_Throws_Bad_Request_Exception() {
        Byte rating = 5;
        CreateReviewRequest request =
                new CreateReviewRequest(author.getId(), reviewer.getId(), rating, "Some content");

        when(reviewRepository.getReviewByReviewerIdAndAuthorId(request.getReviewerId(),
                request.getAuthorId())).thenReturn(true);

        BadRequestException thrownException =
                assertThrows(BadRequestException.class, () -> reviewService.createReview(request));

        Assertions.assertThat(thrownException.getMessage())
                .isEqualTo("You have already reviewed this reviewer");

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    public void ReviewService_GetReviews_ReturnPaginationDtoOfReviewDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        ReviewDto reviewDto = new ReviewDto();
        Page<ReviewDto> pageResult =
                new PageImpl<>(Collections.singletonList(reviewDto), pageable, 1);
        PaginationDto<ReviewDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(reviewRepository.getAllReviewsByReviewerId(pageable, reviewer.getId()))
                .thenReturn(pageResult);

        PaginationDto<ReviewDto> actualPaginationDto =
                reviewService.getReviews(reviewer.getId(), page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto.getItems())
                .isEqualTo(expectedPaginationDto.getItems());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());

    }

    @Test
    public void ReviewService_GetReviewById_ReturnMinReviewDto() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
                    
        MinReviewDto expectedMinReviewDto = new MinReviewDto(review.getId(), review.getRating(), review.getReview()); 
        MinReviewDto actualMinReviewDto = reviewService.getReview(review.getId());

        Assertions.assertThat(actualMinReviewDto).isNotNull();
        Assertions.assertThat(actualMinReviewDto.getId()).isEqualTo(expectedMinReviewDto.getId());
        Assertions.assertThat(actualMinReviewDto.getRating()).isEqualTo(expectedMinReviewDto.getRating());
        Assertions.assertThat(actualMinReviewDto.getReview()).isEqualTo(expectedMinReviewDto.getReview());
    }

    @Test
    public void ReviewService_GetReviewById_Throw_Not_Found_Exception() {
        Long nonExistentReviewId = 999L;
        when(reviewRepository.findById(nonExistentReviewId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.getReview(nonExistentReviewId))
                .isInstanceOf(NotFoundException.class).hasMessage(String
                        .format("A review with the id %d was not found", nonExistentReviewId));
    }

}

