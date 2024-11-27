package com.hart.overwatch.teaminvitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teaminvitation.request.CreateTeamInvitationRequest;
import com.hart.overwatch.teaminvitation.response.CreateTeamInvitationResponse;
import com.hart.overwatch.teaminvitation.response.GetTeamInvitationsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamInvitationController {

    private final TeamInvitationService teamInvitationService;

    @Autowired
    public TeamInvitationController(TeamInvitationService teamInvitationService) {
        this.teamInvitationService = teamInvitationService;
    }

    @PostMapping(path = "/{teamId}/invitations")
    public ResponseEntity<CreateTeamInvitationResponse> createTeamInvitation(
            @Valid @RequestBody CreateTeamInvitationRequest request,
            @PathVariable("teamId") Long teamId) {
        teamInvitationService.createTeamInvitation(request, teamId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateTeamInvitationResponse("success"));
    }

    @GetMapping(path = "/invitations")
    public ResponseEntity<GetTeamInvitationsResponse> getReceiverTeamInvitations(
            @RequestParam("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetTeamInvitationsResponse("success", teamInvitationService
                        .getReceiverTeamInvitations(userId, page, pageSize, direction)));
    }

}
