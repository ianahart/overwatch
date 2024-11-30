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
import com.hart.overwatch.teammember.response.GetTeamMembersResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @Autowired
    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping(path = "/team-members/{userId}/teams")
    public ResponseEntity<GetTeamMemberTeamsResponse> getTeamMemberTeams(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction, @PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(teamMemberService.getTeamMemberTeams(userId, page, pageSize, direction));
    }

    @DeleteMapping(path = "/team-members{teamMemberId}")
    public ResponseEntity<DeleteTeamMemberResponse> deleteTeamMember(
            @PathVariable("teamMemberId") Long teamMemberId) {
        teamMemberService.deleteTeamMember(teamMemberId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTeamMemberResponse("success"));
    }

    @GetMapping(path = "/teams/{teamId}/team-members")
    public ResponseEntity<GetTeamMembersResponse> getTeamMembers(
            @PathVariable("teamId") Long teamId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(teamMemberService.getTeamMembers(teamId, page, pageSize, direction));
    }
}
