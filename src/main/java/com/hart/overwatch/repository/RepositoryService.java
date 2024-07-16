package com.hart.overwatch.repository;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository, UserService userService,
            PaginationService paginationService) {
        this.repositoryRepository = repositoryRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private Repository getRepositoryById(Long repositoryId) {
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
                        request.getLanguage(), RepositoryStatus.INCOMPLETE, reviewer, owner);

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
}

