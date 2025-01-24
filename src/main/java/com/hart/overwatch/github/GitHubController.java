package com.hart.overwatch.github;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.github.response.FetchGitHubAccessTokenResponse;
import com.hart.overwatch.github.response.FetchGitHubUserReposResponse;

@RestController
@RequestMapping(path = "/api/v1/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(path = "/auth")
    public ResponseEntity<FetchGitHubAccessTokenResponse> getAccessToken(
            @RequestParam("code") String code) {
        return ResponseEntity.status(HttpStatus.OK).body(new FetchGitHubAccessTokenResponse(
                "success", this.gitHubService.getAccessToken(code)));
    }

    @GetMapping(path = "/user/repos")
    public ResponseEntity<FetchGitHubUserReposResponse> getUserRepos(
            @RequestParam("githubId") Long githubId, @RequestParam("page") int page)
            throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new FetchGitHubUserReposResponse("success",
                this.gitHubService.getUserRepos(githubId, page)));
    }

}
