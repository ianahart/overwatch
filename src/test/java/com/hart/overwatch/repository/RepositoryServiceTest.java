package com.hart.overwatch.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.github.GitHubService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;


@ExtendWith(MockitoExtension.class)
public class RepositoryServiceTest {
    @InjectMocks
    private RepositoryService repositoryService;

    @Mock
    RepositoryRepository repositoryRepository;

    @Mock
    UserService userService;

    @Mock
    PaginationService paginationService;

    @Mock
    GitHubService gitHubService;

    private User owner;

    private User reviewer;

    private Repository repository;

    private RepositoryDto reviewerRepositoryDto;

    private RepositoryDto ownerRepositoryDto;


    private User createOwner() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/1");
        profile.setId(1L);
        owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        owner.setId(1L);

        return owner;
    }

    private User createReviewer() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/2");
        profile.setId(2L);

        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        reviewer.setId(2L);

        return reviewer;
    }

    private Repository createRepository(User owner, User reviewer) {
        Repository repository = new Repository();

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);

        repository.setId(1L);

        return repository;
    }

    private RepositoryDto createRepositoryDto(User owner, User reviewer, Repository repository,
            Role role) {
        RepositoryDto repositoryDto = new RepositoryDto();

        repositoryDto.setId(repository.getId());
        repositoryDto.setOwnerId(owner.getId());
        repositoryDto.setReviewerId(reviewer.getId());
        repositoryDto
                .setFirstName(role == Role.USER ? reviewer.getFirstName() : owner.getFirstName());
        repositoryDto.setLastName(role == Role.USER ? reviewer.getLastName() : owner.getLastName());
        repositoryDto.setProfileUrl(role == Role.USER ? reviewer.getProfile().getAvatarUrl()
                : owner.getProfile().getAvatarUrl());
        repositoryDto.setRepoName(repository.getRepoName());
        repositoryDto.setLanguage(repository.getLanguage());
        repositoryDto.setRepoUrl(repository.getRepoUrl());
        repositoryDto.setAvatarUrl(repository.getAvatarUrl());
        repositoryDto.setCreatedAt(LocalDateTime.now());
        repositoryDto.setStatus(repository.getStatus());

        return repositoryDto;
    }


    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewerRepositoryDto =
                createRepositoryDto(owner, reviewer, repository, reviewer.getRole());
        ownerRepositoryDto = createRepositoryDto(owner, reviewer, repository, owner.getRole());

    }

    @Test
    public void RepositoryService_HandleCreateUserRepository_ThrowBadRequestException() {
        CreateUserRepositoryRequest request = new CreateUserRepositoryRequest();
        request.setReviewerId(reviewer.getId());
        request.setOwnerId(owner.getId());
        request.setRepoName(repository.getRepoName());
        request.setRepoUrl(repository.getRepoUrl());
        request.setAvatarUrl(repository.getAvatarUrl());
        request.setComment(repository.getComment());
        request.setLanguage(repository.getLanguage());
        when(repositoryRepository.repositoryAlreadyInReview(request.getOwnerId(),
                request.getReviewerId(), request.getRepoName())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            repositoryService.handleCreateUserRepository(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(String
                .format("%s is already being reviewed by this reviewer", request.getRepoName()));
    }

    @Test
    public void RepositoryService_HandleCreateUserRepository_ReturnNothing() {
        CreateUserRepositoryRequest request = new CreateUserRepositoryRequest();
        request.setReviewerId(reviewer.getId());
        request.setOwnerId(owner.getId());
        request.setRepoName(repository.getRepoName());
        request.setRepoUrl(repository.getRepoUrl());
        request.setAvatarUrl(repository.getAvatarUrl());
        request.setComment(repository.getComment());
        request.setLanguage(repository.getLanguage());
        when(repositoryRepository.repositoryAlreadyInReview(request.getOwnerId(),
                request.getReviewerId(), request.getRepoName())).thenReturn(false);

        when(userService.getUserById(request.getOwnerId())).thenReturn(owner);
        when(userService.getUserById(request.getReviewerId())).thenReturn(reviewer);
        when(repositoryRepository.save(any(Repository.class))).thenReturn(repository);

        repositoryService.handleCreateUserRepository(request);

        verify(repositoryRepository, times(1)).save(any(Repository.class));
    }
}


