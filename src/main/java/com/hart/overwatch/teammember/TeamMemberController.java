package com.hart.overwatch.teammember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teammember.response.DeleteTeamMemberResponse;
import com.hart.overwatch.teammember.response.GetTeamMemberTeamsResponse;

@RestController
@RequestMapping(path = "/api/v1/team-members")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @Autowired
    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping(path = "/{userId}/teams")
    public ResponseEntity<GetTeamMemberTeamsResponse> getTeamMemberTeams(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction, @PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(teamMemberService.getTeamMemberTeams(userId, page, pageSize, direction));
    }

    @DeleteMapping(path = "/{teamMemberId}")
    public ResponseEntity<DeleteTeamMemberResponse> deleteTeamMember(
            @PathVariable("teamMemberId") Long teamMemberId) {
        teamMemberService.deleteTeamMember(teamMemberId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTeamMemberResponse("success"));
    }

}
