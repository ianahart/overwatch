package com.hart.overwatch.teampost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teampost.request.CreateTeamPostRequest;
import com.hart.overwatch.teampost.response.CreateTeamPostResponse;
import com.hart.overwatch.teampost.response.DeleteTeamPostResponse;
import com.hart.overwatch.teampost.response.GetTeamPostsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class TeamPostController {

    private final TeamPostService teamPostService;

    @Autowired
    public TeamPostController(TeamPostService teamPostService) {
        this.teamPostService = teamPostService;
    }

    @GetMapping(path = "/teams/{teamId}/team-posts")
    ResponseEntity<GetTeamPostsResponse> getTeamPosts(@PathVariable("teamId") Long teamId,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTeamPostsResponse("success",
                teamPostService.getTeamPosts(teamId, page, pageSize, direction)));
    }

    @PostMapping(path = "/teams/{teamId}/team-posts")
    ResponseEntity<CreateTeamPostResponse> createTeamPost(
            @Valid @RequestBody CreateTeamPostRequest request,
            @PathVariable("teamId") Long teamId) {
        teamPostService.createTeamPost(request, teamId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateTeamPostResponse("success"));
    }

    @DeleteMapping(path = "/team-posts/{teamPostId}")
    ResponseEntity<DeleteTeamPostResponse> deleteTeamPost(
            @PathVariable("teamPostId") Long teamPostId) {
        teamPostService.deleteTeamPost(teamPostId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTeamPostResponse("success"));
    }
}
