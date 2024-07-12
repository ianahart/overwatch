package com.hart.overwatch.github;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.github.response.FetchGitHubAccessTokenResponse;

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

}
