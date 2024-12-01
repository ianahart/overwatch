package com.hart.overwatch.teamcomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teamcomment.dto.MinTeamCommentDto;
import com.hart.overwatch.teamcomment.request.CreateTeamCommentRequest;
import com.hart.overwatch.teamcomment.request.UpdateTeamCommentRequest;
import com.hart.overwatch.teamcomment.response.CreateTeamCommentResponse;
import com.hart.overwatch.teamcomment.response.GetTeamCommentResponse;
import com.hart.overwatch.teamcomment.response.GetTeamCommentsResponse;
import com.hart.overwatch.teamcomment.response.UpdateOrDeleteTeamCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class TeamCommentController {

    private final TeamCommentService teamCommentService;

    @Autowired
    public TeamCommentController(TeamCommentService teamCommentService) {
        this.teamCommentService = teamCommentService;
    }

    @PostMapping(path = "/team-posts/{teamPostId}/team-comments")
    ResponseEntity<CreateTeamCommentResponse> createTeamComment(
            @PathVariable("teamPostId") Long teamPostId,
            @Valid @RequestBody CreateTeamCommentRequest request) {
        teamCommentService.createTeamComment(request, teamPostId);
        return ResponseEntity.status(HttpStatus.OK).body(new CreateTeamCommentResponse("success"));
    }

    @GetMapping(path = "/team-posts/{teamPostId}/team-comments")
    ResponseEntity<GetTeamCommentsResponse> getTeamComments(
            @PathVariable("teamPostId") Long teamPostId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetTeamCommentsResponse("success",
                teamCommentService.getTeamComments(teamPostId, page, pageSize, direction)));
    }

    @GetMapping(path = "/team-posts/{teamPostId}/team-comments/{teamCommentId}")
    ResponseEntity<GetTeamCommentResponse> getTeamComment(
            @PathVariable("teamPostId") Long teamPostId,
            @PathVariable("teamCommentId") Long teamCommentId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTeamCommentResponse("success",
                teamCommentService.getTeamComment(teamCommentId)));
    }

    @PatchMapping(path = "/team-posts/{teamPostId}/team-comments/{teamCommentId}")
    ResponseEntity<UpdateOrDeleteTeamCommentResponse> updateTeamComment(
            @PathVariable("teamPostId") Long teamPostId,
            @PathVariable("teamCommentId") Long teamCommentId,
            @Valid @RequestBody UpdateTeamCommentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateOrDeleteTeamCommentResponse(
                "success", teamCommentService.updateTeamComment(teamCommentId, request)));
    }

    @DeleteMapping(path = "/team-posts/{teamPostId}/team-comments/{teamCommentId}")
    ResponseEntity<UpdateOrDeleteTeamCommentResponse> updateTeamComment(
            @PathVariable("teamPostId") Long teamPostId,
            @PathVariable("teamCommentId") Long teamCommentId) {
        teamCommentService.deleteTeamComment(teamCommentId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new UpdateOrDeleteTeamCommentResponse("success", new MinTeamCommentDto("", "")));
    }

}
