package com.hart.overwatch.repository;


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
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_repository_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class RepositoryRepositoryTest {

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

    private RepositoryDto reviewerRepositoryDto;

    private RepositoryDto ownerRepositoryDto;


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

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);

        repositoryRepository.save(repository);

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

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        repositoryRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void RepositoryRepository_GetReviewerRepositoriesByLanguage_ReturnPageOfRepositoryDto() {
        Pageable pageable = PageRequest.of(0, 3);
        Long reviewerUserId = reviewer.getId();
        String language = repository.getLanguage();
        RepositoryStatus status = repository.getStatus();

        Page<RepositoryDto> result = repositoryRepository
                .getReviewerRepositoriesByLanguage(pageable, reviewerUserId, language, status);

        List<RepositoryDto> actualRepositoryDtos = result.getContent();
        Assertions.assertThat(actualRepositoryDtos.size()).isEqualTo(1);
        RepositoryDto actualRepositoryDto = actualRepositoryDtos.get(0);
        Assertions.assertThat(actualRepositoryDto.getFirstName())
                .isEqualTo(reviewerRepositoryDto.getFirstName());
        Assertions.assertThat(actualRepositoryDto.getLastName())
                .isEqualTo(reviewerRepositoryDto.getLastName());
        Assertions.assertThat(actualRepositoryDto.getProfileUrl())
                .isEqualTo(reviewerRepositoryDto.getProfileUrl());
        Assertions.assertThat(actualRepositoryDto.getLanguage())
                .isEqualTo(reviewerRepositoryDto.getLanguage());
    }

    @Test
    public void RepositoryRepository_GetAllReviewerRepositories_ReturnPageOfRepositoryDto() {
        Pageable pageable = PageRequest.of(0, 3);
        Long reviewerUserId = reviewer.getId();
        RepositoryStatus status = repository.getStatus();

        Page<RepositoryDto> result =
                repositoryRepository.getAllReviewerRepositories(pageable, reviewerUserId, status);

        List<RepositoryDto> actualRepositoryDtos = result.getContent();
        Assertions.assertThat(actualRepositoryDtos.size()).isEqualTo(1);
        RepositoryDto actualRepositoryDto = actualRepositoryDtos.get(0);
        Assertions.assertThat(actualRepositoryDto.getFirstName())
                .isEqualTo(reviewerRepositoryDto.getFirstName());
        Assertions.assertThat(actualRepositoryDto.getLastName())
                .isEqualTo(reviewerRepositoryDto.getLastName());
        Assertions.assertThat(actualRepositoryDto.getProfileUrl())
                .isEqualTo(reviewerRepositoryDto.getProfileUrl());
    }

    @Test
    public void RepositoryRepository_GetOwnerRepositoriesByLanguage_ReturnPageOfRepositoryDto() {
        Pageable pageable = PageRequest.of(0, 3);
        Long ownerUserId = owner.getId();
        String language = repository.getLanguage();
        RepositoryStatus status = repository.getStatus();

        Page<RepositoryDto> result = repositoryRepository.getOwnerRepositoriesByLanguage(pageable,
                ownerUserId, language, status);

        List<RepositoryDto> actualRepositoryDtos = result.getContent();
        Assertions.assertThat(actualRepositoryDtos.size()).isEqualTo(1);
        RepositoryDto actualRepositoryDto = actualRepositoryDtos.get(0);
        Assertions.assertThat(actualRepositoryDto.getFirstName())
                .isEqualTo(ownerRepositoryDto.getFirstName());
        Assertions.assertThat(actualRepositoryDto.getLastName())
                .isEqualTo(ownerRepositoryDto.getLastName());
        Assertions.assertThat(actualRepositoryDto.getProfileUrl())
                .isEqualTo(ownerRepositoryDto.getProfileUrl());
        Assertions.assertThat(actualRepositoryDto.getLanguage())
                .isEqualTo(ownerRepositoryDto.getLanguage());
    }

    @Test
    public void RepositoryRepository_GetAllOwnerRepositories_ReturnPageOfRepositoryDto() {
        Pageable pageable = PageRequest.of(0, 3);
        Long ownerUserId = owner.getId();
        RepositoryStatus status = repository.getStatus();

        Page<RepositoryDto> result = repositoryRepository.getAllOwnerRepositories(pageable,
                ownerUserId,  status);

        List<RepositoryDto> actualRepositoryDtos = result.getContent();
        Assertions.assertThat(actualRepositoryDtos.size()).isEqualTo(1);
        RepositoryDto actualRepositoryDto = actualRepositoryDtos.get(0);
        Assertions.assertThat(actualRepositoryDto.getFirstName())
                .isEqualTo(ownerRepositoryDto.getFirstName());
        Assertions.assertThat(actualRepositoryDto.getLastName())
                .isEqualTo(ownerRepositoryDto.getLastName());
        Assertions.assertThat(actualRepositoryDto.getProfileUrl())
                .isEqualTo(ownerRepositoryDto.getProfileUrl());
        Assertions.assertThat(actualRepositoryDto.getLanguage())
                .isEqualTo(ownerRepositoryDto.getLanguage());
    }


}


