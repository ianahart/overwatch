package com.hart.overwatch.teampinnedmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.response.CreateTeamPinnedMessageResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamPinnedMessageController {

    private final TeamPinnedMessageService teamPinnedMessageService;

    @Autowired
    public TeamPinnedMessageController(TeamPinnedMessageService teamPinnedMessageService) {
        this.teamPinnedMessageService = teamPinnedMessageService;
    }

    @PostMapping(path = "/{teamId}/team-pinned-messages")
    public ResponseEntity<CreateTeamPinnedMessageResponse> createTeamPinnedMessage(
            @PathVariable("teamId") Long teamId,
            @Valid @RequestBody CreateTeamPinnedMessageRequest request) {
        teamPinnedMessageService.createTeamPinnedMessage(teamId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateTeamPinnedMessageResponse("success"));
    }
}
