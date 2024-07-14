package com.hart.overwatch.repository;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;

    private final UserService userService;

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository, UserService userService) {
        this.repositoryRepository = repositoryRepository;
        this.userService = userService;
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
                        RepositoryStatus.INCOMPLETE, reviewer, owner);

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
}
