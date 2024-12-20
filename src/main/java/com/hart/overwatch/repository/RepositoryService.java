package com.hart.overwatch.repository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.github.GitHubService;
import com.hart.overwatch.github.dto.GitHubTreeDto;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.repository.dto.CompletedRepositoryReviewDto;
import com.hart.overwatch.repository.dto.FullRepositoryDto;
import com.hart.overwatch.repository.dto.RepositoryContentsDto;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.dto.RepositoryLanguageDto;
import com.hart.overwatch.repository.dto.RepositoryReviewDto;
import com.hart.overwatch.repository.dto.RepositoryStatusDto;
import com.hart.overwatch.repository.dto.RepositoryTopRequesterDto;
import com.hart.overwatch.repository.request.CreateRepositoryFileRequest;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewStartTimeRequest;
import com.hart.overwatch.reviewerbadge.ReviewerBadgeService;
import com.hart.overwatch.reviewfeedback.ReviewFeedbackService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    private final GitHubService gitHubService;

    private final ReviewFeedbackService reviewFeedbackService;

    private final ReviewerBadgeService reviewerBadgeService;

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository, UserService userService,
            PaginationService paginationService, GitHubService gitHubService,
            @Lazy ReviewFeedbackService reviewFeedbackService,

            ReviewerBadgeService reviewerBadgeService) {
        this.repositoryRepository = repositoryRepository;
        this.userService = userService;
        this.paginationService = paginationService;
        this.gitHubService = gitHubService;
        this.reviewFeedbackService = reviewFeedbackService;
        this.reviewerBadgeService = reviewerBadgeService;
    }

    public Repository getRepositoryById(Long repositoryId) {
        return this.repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Repository with the id %d was not found", repositoryId)));
    }

    private boolean repositoryAlreadyInReview(Long ownerId, Long reviewerId, String repoName) {
        try {
            if (ownerId == null || reviewerId == null) {
                throw new BadRequestException("ownerId or reviewerId is missing or null");
            }
            return this.repositoryRepository.repositoryAlreadyInReview(ownerId, reviewerId,
                    repoName);

        } catch (DataAccessException ex) {
            return true;
        }
    }

    private void createUserRepository(CreateUserRepositoryRequest request) {
        User owner = this.userService.getUserById(request.getOwnerId());
        User reviewer = this.userService.getUserById(request.getReviewerId());

        String feedback = "";

        Repository repository =
                new Repository(feedback, Jsoup.clean(request.getComment(), Safelist.none()),
                        request.getAvatarUrl(), request.getRepoUrl(), request.getRepoName(),
                        request.getLanguage(), RepositoryStatus.INCOMPLETE, reviewer, owner,
                        request.getReviewType(), request.getPaymentPrice());

        this.repositoryRepository.save(repository);

    }

    public void handleCreateUserRepository(CreateUserRepositoryRequest request) {
        if (repositoryAlreadyInReview(request.getOwnerId(), request.getReviewerId(),
                request.getRepoName())) {
            throw new BadRequestException(String.format(
                    "%s is already being reviewed by this reviewer", request.getRepoName()));
        }
        createUserRepository(request);
    }


    private List<String> getReviewerDistinctRepositoryLanguages(Long userId) {
        return this.repositoryRepository.getReviewerDistinctRepositoryLanguages(userId);
    }


    private List<String> getOwnerDistinctRepositoryLanguages(Long userId) {
        return this.repositoryRepository.getOwnerDistinctRepositoryLanguages(userId);
    }


    public List<String> getDistinctRepositoryLanguages() {
        User currentUser = this.userService.getCurrentlyLoggedInUser();
        List<String> languages = currentUser.getRole() == Role.REVIEWER
                ? getReviewerDistinctRepositoryLanguages(currentUser.getId())
                : getOwnerDistinctRepositoryLanguages(currentUser.getId());
        languages.add("All");
        return languages;
    }

    public PaginationDto<RepositoryDto> getAllRepositories(int page, int pageSize, String direction,
            String sort, RepositoryStatus status, String language) {

        User currentUser = this.userService.getCurrentlyLoggedInUser();
        Pageable pageable =
                this.paginationService.getSortedPageable(page, pageSize, direction, sort);


        Page<RepositoryDto> queryResult = null;
        if (currentUser.getRole() == Role.REVIEWER && language.toLowerCase().equals("all")) {

            queryResult = this.repositoryRepository.getAllReviewerRepositories(pageable,
                    currentUser.getId(), status);

        } else if (currentUser.getRole() == Role.REVIEWER
                && !language.toLowerCase().equals("all")) {

            queryResult = this.repositoryRepository.getReviewerRepositoriesByLanguage(pageable,
                    currentUser.getId(), language, status);

        } else if (currentUser.getRole() == Role.USER && language.toLowerCase().equals("all")) {

            queryResult = this.repositoryRepository.getAllOwnerRepositories(pageable,
                    currentUser.getId(), status);
        } else {
            queryResult = this.repositoryRepository.getOwnerRepositoriesByLanguage(pageable,
                    currentUser.getId(), language, status);
        }

        return new PaginationDto<RepositoryDto>(queryResult.getContent(), queryResult.getNumber(),
                pageSize, queryResult.getTotalPages(), direction, queryResult.getTotalElements());
    }


    public void deleteRepository(Long repositoryId) {
        try {
            Long currentUserId = this.userService.getCurrentlyLoggedInUser().getId();
            Repository repository = getRepositoryById(repositoryId);

            if (currentUserId != repository.getOwner().getId()) {
                throw new ForbiddenException("Cannot delete a repository that is not yours");
            }

            this.repositoryRepository.delete(repository);

        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    public String getRepositoryComment(Long repositoryId) {
        try {
            Repository repository = getRepositoryById(repositoryId);
            return repository.getComment();

        } catch (DataAccessException ex) {
            throw ex;
        }
    }

    public void updateRepositoryComment(Long repositoryId, String comment) {
        try {
            Repository repository = getRepositoryById(repositoryId);

            String cleanedComment = Jsoup.clean(comment, Safelist.none());

            repository.setComment(cleanedComment);

            this.repositoryRepository.save(repository);

        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    private String createReviewDuration(Repository repository) {
        LocalDateTime reviewEndTime = repository.getReviewEndTime() == null ? LocalDateTime.now()
                : repository.getReviewEndTime();

        if (repository.getReviewStartTime() == null || reviewEndTime == null) {
            return String.format("%dh%dm%ds", 0, 0, 0);
        }
        Duration duration = Duration.between(repository.getReviewStartTime(), reviewEndTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%dh%dm%ds", hours, minutes, seconds);
    }

    private FullRepositoryDto constructRepository(Repository entity) {
        String formattedReviewDuration = createReviewDuration(entity);
        return new FullRepositoryDto(entity.getId(), entity.getOwner().getId(),
                entity.getReviewer().getId(), entity.getComment(), entity.getRepoUrl(),
                entity.getFeedback(), entity.getLanguage(), entity.getRepoName(),
                entity.getAvatarUrl(), entity.getStatus(), entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getReviewStartTime(), entity.getReviewEndTime(),
                entity.getReviewType(), formattedReviewDuration);
    }


    public RepositoryContentsDto searchRepository(String accessToken, int page, int size,
            String repoName, String query) throws IOException {
        try {
            FullRepositoryDto repository = new FullRepositoryDto();
            GitHubTreeDto contents =
                    gitHubService.searchRepository(accessToken, page, query, repoName, size);
            return new RepositoryContentsDto(repository, contents);

        } catch (DataAccessException | IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }


    public RepositoryContentsDto getRepositoryReview(Long repositoryId, String accessToken,
            int page, int size) throws IOException {
        try {
            Repository entity = getRepositoryById(repositoryId);
            FullRepositoryDto repository = constructRepository(entity);
            GitHubTreeDto contents = this.gitHubService.getRepository(repository.getRepoName(),
                    accessToken, page, size);
            return new RepositoryContentsDto(repository, contents);

        } catch (DataAccessException ex) {
            throw ex;
        }
    }

    public String getRepositoryFile(CreateRepositoryFileRequest request) throws IOException {
        return this.gitHubService.getRepositoryFile(request.getAccessToken(), request.getPath(),
                request.getOwner(), request.getRepoName());
    }


    public RepositoryReviewDto updateRepositoryReview(Long repositoryId,
            UpdateRepositoryReviewRequest request) {
        try {
            Repository repository = getRepositoryById(repositoryId);
            User reviewer = this.userService.getCurrentlyLoggedInUser();

            if (repository.getReviewer().getId() != reviewer.getId()) {
                throw new ForbiddenException("Cannot update a review that is not yours");
            }

            repository.setFeedback(request.getFeedback());
            repository.setStatus(request.getStatus());

            if (request.getStatus() == RepositoryStatus.COMPLETED) {
                LocalDateTime reviewEndTime = LocalDateTime.now();
                repository.setReviewEndTime(reviewEndTime);
                repositoryRepository.save(repository);
                cycleThroughBadges(reviewer);
            } else {
                this.repositoryRepository.save(repository);

            }
            return new RepositoryReviewDto(repository.getStatus(), repository.getFeedback());

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void updateRepositoryReviewStartTime(Long repositoryId,
            UpdateRepositoryReviewStartTimeRequest request) {
        Repository repository = getRepositoryById(repositoryId);

        if (request.getReviewStartTime() != null
                && repository.getStatus() != RepositoryStatus.INCOMPLETE) {
            return;
        }

        LocalDateTime reviewStartTime = LocalDateTime.now();
        repository.setReviewStartTime(reviewStartTime);

        repositoryRepository.save(repository);
    }

    public List<CompletedRepositoryReviewDto> getCompletedReviews(Long reviewerId) {
        return repositoryRepository.findByReviewerIdAndCompleted(reviewerId,
                RepositoryStatus.COMPLETED);
    }

    public List<RepositoryLanguageDto> getMainLanguages(Long reviewerId) {
        return repositoryRepository.getMainLanguages(reviewerId);
    }

    public List<RepositoryStatusDto> getAllRepositoriesWithStatuses(Long reviewerId) {
        return repositoryRepository.findStatusesByReviewerId(reviewerId);
    }

    public List<RepositoryTopRequesterDto> getTopRequestersOfReviewer(Long reviewerId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                .atTime(LocalTime.MAX);

        return repositoryRepository.findOwnersByReviewerId(reviewerId, startOfMonth, endOfMonth);
    }

    public void updateStatus(Long repositoryId, RepositoryStatus status) {
        Repository repository = getRepositoryById(repositoryId);
        repository.setStatus(status);
        repositoryRepository.save(repository);
    }

    private List<Repository> getCompletedReviewerRepositories(User reviewer) {
        if (reviewer.getReviewerRepositories().isEmpty()) {
            List<Repository> emptyList = new ArrayList<>();
            return emptyList;
        }

        return reviewer.getReviewerRepositories().stream()
                .filter(repo -> repo.getStatus() == RepositoryStatus.COMPLETED
                        || repo.getStatus() == RepositoryStatus.PAID)
                .collect(Collectors.toList());
    }

    private boolean firstReviewBadge(List<Repository> repositories) {
        return repositories.size() >= 1;
    }

    private boolean tenReviewsBadge(List<Repository> repositories) {
        return repositories.size() >= 10;
    }

    private boolean fiftyReviewsBadge(List<Repository> repositories) {
        return repositories.size() >= 50;
    }

    private boolean oneHundredReviewsBadge(List<Repository> repositories) {
        return repositories.size() >= 100;
    }

    private boolean consistentReviewBadge(Long reviewerId) {
        int consistentWeeks = repositoryRepository.countConsistentWeeks(reviewerId);
        return consistentWeeks >= 4;
    }

    private boolean highQualityFeedbackBadge(User reviewer) {
        return reviewer.getReviewerReviewFeedbacks().stream()
                .anyMatch(review -> review.getThoroughness() >= 4);
    }

    private boolean mentorBadge(User reviewer) {
        List<Long> mentorIds = reviewFeedbackService.getMentorEligibleReviewers();
        return mentorIds.contains(reviewer.getId());
    }

    private boolean communityBuilderBadge(User reviewer) {
        int developerCount = 0;
        for (var feedback : reviewer.getReviewerReviewFeedbacks()) {
            if (feedback.getClarity() > 2 && feedback.getHelpfulness() > 2
                    && feedback.getThoroughness() > 2 && feedback.getResponseTime() > 2) {
                developerCount++;
            }
        }
        return developerCount >= 1;
    }

    private boolean speedyReviewerBadge(User reviewer) {
        int count = 0;
        for (Repository repository : getCompletedReviewerRepositories(reviewer)) {
            LocalDateTime startTime = repository.getReviewStartTime();
            LocalDateTime endTime = repository.getReviewEndTime();

            boolean hasHourPassed = ChronoUnit.HOURS.between(startTime, endTime) >= 1;

            if (hasHourPassed) {
                count++;
            }
        }
        return count >= 10;
    }

    private void cycleThroughBadges(User reviewer) {
        List<Repository> repositories = getCompletedReviewerRepositories(reviewer);

        if (firstReviewBadge(repositories)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "First Review Badge")) {
            reviewerBadgeService.createBadge(reviewer, "First Review Badge");

        }

        if (tenReviewsBadge(repositories)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Ten Reviews Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Ten Reviews Badge");
        }

        if (consistentReviewBadge(reviewer.getId())
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Consistent Reviewer Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Consistent Reviewer Badge");
        }

        if (highQualityFeedbackBadge(reviewer) && !reviewerBadgeService.hasBadge(reviewer.getId(),
                "High-Quality Feedback Badge")) {
            reviewerBadgeService.createBadge(reviewer, "High-Quality Feedback Badge");
        }

        if (fiftyReviewsBadge(repositories)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Fifty Reviews Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Fifty Reviews Badge");
        }

        if (mentorBadge(reviewer)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Mentor Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Mentor Badge");
        }

        if (speedyReviewerBadge(reviewer)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Speedy Reviewer Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Speedy Reviewer Badge");
        }

        if (oneHundredReviewsBadge(repositories)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Hundred Reviews Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Hundred Reviews Badge");
        }

        if (communityBuilderBadge(reviewer)
                && !reviewerBadgeService.hasBadge(reviewer.getId(), "Community Builder Badge")) {
            reviewerBadgeService.createBadge(reviewer, "Community Builder Badge");
        }
    }

}

