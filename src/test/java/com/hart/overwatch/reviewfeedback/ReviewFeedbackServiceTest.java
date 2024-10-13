package com.hart.overwatch.reviewfeedback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.repository.RepositoryService;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;
import com.hart.overwatch.reviewfeedback.request.CreateReviewFeedbackRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;


@ExtendWith(MockitoExtension.class)
public class ReviewFeedbackServiceTest {
    @InjectMocks
    private ReviewFeedbackService reviewFeedbackService;

    @Mock
    ReviewFeedbackRepository reviewFeedbackRepository;

    @Mock
    UserService userService;

    @Mock
    RepositoryService repositoryService;

    private User owner;

    private User reviewer;

    private Repository repository;

    private ReviewFeedback reviewFeedback;

    private User createOwner() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setAvatarUrl("http://avatar.url/1");

        owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        owner.setId(1L);

        return owner;
    }

    private User createReviewer() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setId(2L);
        profile.setAvatarUrl("http://avatar.url/2");

        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        reviewer.setId(2L);

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

        repository.setId(1L);

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

        reviewFeedback.setId(1L);

        return reviewFeedback;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewFeedback = createReviewFeedback(owner, reviewer, repository);
    }

    @Test
    public void ReviewFeedbackService_GetSingleReviewFeedback_ThrowBadRequestExceptionMissingParams() {
        Long ownerId = owner.getId();
        Long reviewerId = null;
        Long repositoryId = repository.getId();

        Assertions.assertThatThrownBy(() -> {
            reviewFeedbackService.getSingleReviewFeedback(ownerId, reviewerId, repositoryId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing parameters: ownerId, reviewerId, or repositoryId");
    }

    @Test
    public void ReviewFeedbackService_GetSingleReviewFeedback_ThrowForbiddenException() {
        Long ownerId = owner.getId();
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();

        User currentUser = new User();
        currentUser.setId(999L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(currentUser);

        Assertions.assertThatThrownBy(() -> {
            reviewFeedbackService.getSingleReviewFeedback(ownerId, reviewerId, repositoryId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You are unauthorized to see feedback that is not yours");
    }

    @Test
    public void ReviewFeedbackService_GetSingleReviewFeedback_ReturnReviewFeedbackDto() {
        Long ownerId = owner.getId();
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();

        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);
        ReviewFeedbackDto reviewFeedbackDto = new ReviewFeedbackDto();
        reviewFeedbackDto.setOwnerId(ownerId);
        reviewFeedbackDto.setReviewerId(reviewerId);
        reviewFeedbackDto.setRepositoryId(repositoryId);
        reviewFeedback.setClarity(1);
        reviewFeedbackDto.setHelpfulness(1);
        reviewFeedbackDto.setResponseTime(1);
        reviewFeedbackDto.setThoroughness(1);

        when(reviewFeedbackRepository.getSingleReviewFeedback(ownerId, reviewerId, repositoryId))
                .thenReturn(reviewFeedbackDto);

        ReviewFeedbackDto returnedReviewFeedbackDto =
                reviewFeedbackService.getSingleReviewFeedback(ownerId, reviewerId, repositoryId);

        Assertions.assertThat(returnedReviewFeedbackDto).isNotNull();
        Assertions.assertThat(reviewFeedbackDto.getOwnerId())
                .isEqualTo(returnedReviewFeedbackDto.getOwnerId());
        Assertions.assertThat(reviewFeedbackDto.getReviewerId())
                .isEqualTo(returnedReviewFeedbackDto.getReviewerId());
        Assertions.assertThat(reviewFeedbackDto.getRepositoryId())
                .isEqualTo(returnedReviewFeedbackDto.getRepositoryId());
        Assertions.assertThat(reviewFeedbackDto.getClarity())
                .isEqualTo(returnedReviewFeedbackDto.getClarity());
        Assertions.assertThat(reviewFeedbackDto.getHelpfulness())
                .isEqualTo(returnedReviewFeedbackDto.getHelpfulness());
        Assertions.assertThat(reviewFeedbackDto.getResponseTime())
                .isEqualTo(returnedReviewFeedbackDto.getResponseTime());
        Assertions.assertThat(reviewFeedbackDto.getThoroughness())
                .isEqualTo(returnedReviewFeedbackDto.getThoroughness());

    }


    @Test
    public void ReviewFeedbackService_CreateReviewFeedback_Throw_BadRequestExceptionMissingParams() {
        Long ownerId = owner.getId();
        Long reviewerId = null;
        Long repositoryId = repository.getId();
        CreateReviewFeedbackRequest request = new CreateReviewFeedbackRequest();
        request.setOwnerId(ownerId);
        request.setReviewerId(reviewerId);
        request.setRepositoryId(repositoryId);
        request.setClarity(1);
        request.setHelpfulness(1);
        request.setResponseTime(1);
        request.setThoroughness(1);

        Assertions.assertThatThrownBy(() -> {
            reviewFeedbackService.createReviewFeedback(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing parameters: ownerId, reviewerId, or repositoryId");
    }

    @Test
    public void ReviewFeedbackService_CreateReviewFeedback_Throw_BadRequestExceptionExists() {
        Long ownerId = owner.getId();
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();
        CreateReviewFeedbackRequest request = new CreateReviewFeedbackRequest();
        request.setOwnerId(ownerId);
        request.setReviewerId(reviewerId);
        request.setRepositoryId(repositoryId);
        request.setClarity(1);
        request.setHelpfulness(1);
        request.setResponseTime(1);
        request.setThoroughness(1);

        when(reviewFeedbackRepository.findByOwnerIdAndReviewerIdAndRepositoryId(ownerId, reviewerId,
                repositoryId)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            reviewFeedbackService.createReviewFeedback(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already given feedback for this review");
    }

    @Test
    public void ReviewFeedbackService_CreateReviewFeedback_ThrowForbiddenException() {
        User forbiddenOwner = new User();
        forbiddenOwner.setId(999L);
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();
        CreateReviewFeedbackRequest request = new CreateReviewFeedbackRequest();
        request.setOwnerId(forbiddenOwner.getId());
        request.setReviewerId(reviewerId);
        request.setRepositoryId(repositoryId);
        request.setClarity(1);
        request.setHelpfulness(1);
        request.setResponseTime(1);
        request.setThoroughness(1);

        when(reviewFeedbackRepository.findByOwnerIdAndReviewerIdAndRepositoryId(
                forbiddenOwner.getId(), reviewerId, repositoryId)).thenReturn(false);
        when(userService.getUserById(forbiddenOwner.getId())).thenReturn(forbiddenOwner);
        when(userService.getUserById(reviewerId)).thenReturn(reviewer);
        when(repositoryService.getRepositoryById(repositoryId)).thenReturn(repository);

        Assertions.assertThatThrownBy(() -> {
            reviewFeedbackService.createReviewFeedback(request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You are not authorized to submit feedback for this repository.");

    }
}


