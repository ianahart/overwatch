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

}


