package com.hart.overwatch.repository;

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
import com.hart.overwatch.github.GitHubService;
import com.hart.overwatch.github.dto.GitHubTreeDto;
import com.hart.overwatch.github.dto.GitHubTreeNodeDto;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.repository.dto.FullRepositoryDto;
import com.hart.overwatch.repository.dto.RepositoryContentsDto;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewRequest;
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

    private PaginationDto<RepositoryDto> reviewerPaginationDto;

    private PaginationDto<RepositoryDto> ownerPaginationDto;

    private Page<RepositoryDto> pageResult;


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
        LocalDateTime reviewTimes = LocalDateTime.now();

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);
        repository.setReviewStartTime(reviewTimes);
        repository.setReviewEndTime(reviewTimes);

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
        repositoryDto.setReviewEndTime(LocalDateTime.now());
        repositoryDto.setReviewStartTime(LocalDateTime.now());
        repositoryDto.setStatus(repository.getStatus());

        return repositoryDto;
    }

    private Pageable createPageable(int pageSize) {
        return PageRequest.of(0, pageSize);
    }

    private PaginationDto<RepositoryDto> createPaginationDto(RepositoryDto repositoryDto) {
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = createPageable(pageSize);

        pageResult = new PageImpl<>(Collections.singletonList(repositoryDto), pageable, 1);
        PaginationDto<RepositoryDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        return expectedPaginationDto;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewerRepositoryDto =
                createRepositoryDto(owner, reviewer, repository, reviewer.getRole());
        ownerRepositoryDto = createRepositoryDto(owner, reviewer, repository, owner.getRole());
        reviewerPaginationDto = createPaginationDto(reviewerRepositoryDto);
        ownerPaginationDto = createPaginationDto(ownerRepositoryDto);

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

    @Test
    public void RepositoryService_GetDistinctRepositoryLanguagesOwner_ReturnListOfString() {
        List<String> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(repository.getLanguage());
        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);
        when(repositoryRepository.getOwnerDistinctRepositoryLanguages(owner.getId()))
                .thenReturn(expectedLanguages);

        List<String> actualLanguages = repositoryService.getDistinctRepositoryLanguages();

        Assertions.assertThat(actualLanguages).isNotNull();
        Assertions.assertThat(actualLanguages.size()).isEqualTo(2);
        Assertions.assertThat(actualLanguages.get(0)).isEqualTo(expectedLanguages.get(0));
        Assertions.assertThat(actualLanguages.get(1)).isEqualTo("All");
    }

    @Test
    public void RepositoryService_GetDistinctRepositoryLanguagesReviewer_ReturnListOfString() {
        List<String> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(repository.getLanguage());
        when(userService.getCurrentlyLoggedInUser()).thenReturn(reviewer);
        when(repositoryRepository.getReviewerDistinctRepositoryLanguages(reviewer.getId()))
                .thenReturn(expectedLanguages);

        List<String> actualLanguages = repositoryService.getDistinctRepositoryLanguages();

        Assertions.assertThat(actualLanguages).isNotNull();
        Assertions.assertThat(actualLanguages.size()).isEqualTo(2);
        Assertions.assertThat(actualLanguages.get(0)).isEqualTo(expectedLanguages.get(0));
        Assertions.assertThat(actualLanguages.get(1)).isEqualTo("All");
    }

    @Test
    public void RepositoryService_GetAllRepositoriesGetAllReviewerRepositories_ReturnPaginationDtoOfRepositoryDto() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(reviewer);
        Pageable pageable = createPageable(3);
        when(paginationService.getSortedPageable(reviewerPaginationDto.getPage(), reviewerPaginationDto.getPageSize(), "next", "desc")).thenReturn(pageable);
        when(repositoryRepository.getAllReviewerRepositories(pageable, reviewer.getId(), RepositoryStatus.INCOMPLETE)).thenReturn(pageResult);

        PaginationDto<RepositoryDto> result = repositoryService.getAllRepositories(0,3,"next", "desc", RepositoryStatus.INCOMPLETE, "all");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getPage()).isEqualTo(0);
        Assertions.assertThat(result.getPageSize()).isEqualTo(3);
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    public void RepositoryService_GetAllRepositoriesGetAllReviewerRepositoriesByLanguage_ReturnPaginationDtoOfRepositoryDto() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(reviewer);
        Pageable pageable = createPageable(3);
        when(paginationService.getSortedPageable(reviewerPaginationDto.getPage(), reviewerPaginationDto.getPageSize(), "next", "desc")).thenReturn(pageable);
        when(repositoryRepository.getReviewerRepositoriesByLanguage(pageable, reviewer.getId(), "Java", RepositoryStatus.INCOMPLETE)).thenReturn(pageResult);

        PaginationDto<RepositoryDto> result = repositoryService.getAllRepositories(0,3,"next", "desc", RepositoryStatus.INCOMPLETE, "Java");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getPage()).isEqualTo(0);
        Assertions.assertThat(result.getPageSize()).isEqualTo(3);
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    public void RepositoryService_GetAllRepositoriesGetAllOwnerRepositories_ReturnPaginationDtoOfRepositoryDto() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);
        Pageable pageable = createPageable(3);
        when(paginationService.getSortedPageable(ownerPaginationDto.getPage(), ownerPaginationDto.getPageSize(), "next", "desc")).thenReturn(pageable);
        when(repositoryRepository.getAllOwnerRepositories(pageable, owner.getId(), RepositoryStatus.INCOMPLETE)).thenReturn(pageResult);

        PaginationDto<RepositoryDto> result = repositoryService.getAllRepositories(0,3,"next", "desc", RepositoryStatus.INCOMPLETE, "all");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getPage()).isEqualTo(0);
        Assertions.assertThat(result.getPageSize()).isEqualTo(3);
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    public void RepositoryService_GetAllRepositoriesGetAllOwnerRepositoriesByLanguage_ReturnPaginationDtoOfRepositoryDto() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);
        Pageable pageable = createPageable(3);
        when(paginationService.getSortedPageable(ownerPaginationDto.getPage(), ownerPaginationDto.getPageSize(), "next", "desc")).thenReturn(pageable);
        when(repositoryRepository.getOwnerRepositoriesByLanguage(pageable, owner.getId(), "Java", RepositoryStatus.INCOMPLETE)).thenReturn(pageResult);

        PaginationDto<RepositoryDto> result = repositoryService.getAllRepositories(0,3,"next", "desc", RepositoryStatus.INCOMPLETE, "Java");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getPage()).isEqualTo(0);
        Assertions.assertThat(result.getPageSize()).isEqualTo(3);
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    public void RepositoryService_DeleteRepository_ThrowForbiddenException() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(reviewer);
        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));

        Assertions.assertThatThrownBy(() -> {
            repositoryService.deleteRepository(repository.getId());
        }).isInstanceOf(ForbiddenException.class).hasMessage("Cannot delete a repository that is not yours");
    }

    @Test
    public void RepositoryService_DeleteRepository_DoNothing() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);
        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));
        doNothing().when(repositoryRepository).delete(repository);

        repositoryService.deleteRepository(repository.getId());

        verify(userService, times(1)).getCurrentlyLoggedInUser();
        verify(repositoryRepository, times(1)).delete(repository);;
    }

    @Test
    public void RepositoryService_GetRepositoryComment_ReturnStringComment() {
        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));

        String repositoryComment = repositoryService.getRepositoryComment(repository.getId());

        Assertions.assertThat(repositoryComment).isNotNull();
        Assertions.assertThat(repositoryComment).isEqualTo(repository.getComment());
    }

    @Test
    public void RepositoryService_UpdateRepositoryComment_ReturnNothing() {
        Repository updatedRepository = new Repository();
        updatedRepository.setComment("current comment");
        updatedRepository.setId(2L);

        when(repositoryRepository.findById(updatedRepository.getId()))
                .thenReturn(Optional.of(updatedRepository));
        when(repositoryRepository.save(any(Repository.class))).thenReturn(updatedRepository);

        repositoryService.updateRepositoryComment(updatedRepository.getId(), "updated comment");

        verify(repositoryRepository, times(1)).save(any(Repository.class));
    }

    @Test
    public void RepositoryService_GetRepositoryReview_ReturnRepositoryContentsDto()
            throws IOException {
        RepositoryContentsDto repositoryContentsDto = new RepositoryContentsDto();
        FullRepositoryDto fullRepositoryDto = new FullRepositoryDto();
        fullRepositoryDto.setId(repository.getId());
        fullRepositoryDto.setOwnerId(owner.getId());
        fullRepositoryDto.setReviewerId(reviewer.getId());
        fullRepositoryDto.setComment(repository.getComment());
        fullRepositoryDto.setRepoUrl(repository.getRepoUrl());
        fullRepositoryDto.setFeedback(repository.getFeedback());
        fullRepositoryDto.setLanguage(repository.getLanguage());
        fullRepositoryDto.setRepoName(repository.getRepoName());
        fullRepositoryDto.setAvatarUrl(repository.getAvatarUrl());
        fullRepositoryDto.setStatus(repository.getStatus());
        fullRepositoryDto.setCreatedAt(repository.getCreatedAt());
        fullRepositoryDto.setUpdatedAt(repository.getCreatedAt());
        fullRepositoryDto.setReviewDuration("0h0m0s");
        fullRepositoryDto.setReviewStartTime(repository.getReviewEndTime());
        fullRepositoryDto.setReviewEndTime(repository.getReviewEndTime());
        GitHubTreeDto gitHubTreeDto = new GitHubTreeDto();
        gitHubTreeDto.setLanguages(List.of("Java", "HTML", "Python"));
        GitHubTreeNodeDto gitHubTreeNodeDto = new GitHubTreeNodeDto();
        gitHubTreeNodeDto.setSha("234sdfljdsj234k5");
        gitHubTreeNodeDto.setUrl("https://github.com/react");
        gitHubTreeNodeDto.setPath("dsf/sdf/dsf/ds/");
        gitHubTreeNodeDto.setType("file");
        gitHubTreeNodeDto.setSize(2);
        gitHubTreeDto.setTree(List.of(gitHubTreeNodeDto));
        repositoryContentsDto.setRepository(fullRepositoryDto);
        repositoryContentsDto.setContents(gitHubTreeDto);

        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));
        when(gitHubService.getRepository(repository.getRepoName(), "dummy_github_access_token", 1,
                10)).thenReturn(gitHubTreeDto);

        RepositoryContentsDto result = repositoryService.getRepositoryReview(repository.getId(),
                "dummy_github_access_token", 1, 10);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getRepository()).usingRecursiveComparison()
                .isEqualTo(repositoryContentsDto.getRepository());
        Assertions.assertThat(result.getContents()).usingRecursiveComparison()
                .isEqualTo(repositoryContentsDto.getContents());
    }

    @Test
    public void RepositoryService_UpdateRepositoryReview_ThrowForbiddenException() {
        UpdateRepositoryReviewRequest request =
                new UpdateRepositoryReviewRequest(RepositoryStatus.INPROGRESS, "some feedback");
        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(owner);

        Assertions.assertThatThrownBy(() -> {
            repositoryService.updateRepositoryReview(repository.getId(), request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot update a review that is not yours");
    }

    @Test
    public void RepositoryService_UpdateRepositoryReview_Return_RepositoryReviewDto() {
        UpdateRepositoryReviewRequest request =
                new UpdateRepositoryReviewRequest(RepositoryStatus.INPROGRESS, "some feedback");
        when(repositoryRepository.findById(repository.getId())).thenReturn(Optional.of(repository));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(reviewer);
        when(repositoryRepository.save(any(Repository.class))).thenReturn(repository);

        repositoryService.updateRepositoryReview(repository.getId(), request);


        verify(repositoryRepository, times(1)).save(any(Repository.class));

    }
}


