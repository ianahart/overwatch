package com.hart.overwatch.githubtoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class GitHubTokenService {

    private final UserService userService;

    private final GitHubTokenRepository gitHubTokenRepository;

    @Autowired
    public GitHubTokenService(UserService userService,
            GitHubTokenRepository gitHubTokenRepository) {
        this.userService = userService;
        this.gitHubTokenRepository = gitHubTokenRepository;
    }

    private GitHubToken getGitHubTokenById(Long githubId) {
        return gitHubTokenRepository.findById(githubId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find a github id %d", githubId)));
    }

    public void deleteGitHubToken(Long userId) {
        if (userId != null) {
            gitHubTokenRepository.deleteByUserId(userId);
        }
    }


    public Long createGitHubToken(String accessToken) {
        User currentUser = userService.getCurrentlyLoggedInUser();
        if (currentUser.getGithubTokens() != null && currentUser.getGithubTokens().size() > 0) {
            deleteGitHubToken(currentUser.getId());
        }

        if (accessToken.isEmpty()) {
            throw new BadRequestException("Could not create Github token without access token");
        }

        GitHubToken githubToken = new GitHubToken(accessToken, currentUser);

        gitHubTokenRepository.save(githubToken);

        return githubToken.getId();
    }

    public String getGitHubToken(Long githubId) {
        User user = userService.getCurrentlyLoggedInUser();
        GitHubToken githubToken = getGitHubTokenById(githubId);

        if (!user.getId().equals(githubToken.getUser().getId())) {
            throw new ForbiddenException("Cannot use another user's github token");
        }

        return githubToken.getAccessToken();
    }
}
