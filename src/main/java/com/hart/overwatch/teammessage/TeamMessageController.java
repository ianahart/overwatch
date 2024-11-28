package com.hart.overwatch.teammessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teammessage.response.GetTeamMessagesResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class TeamMessageController {

    private final TeamMessageService teamMessageService;

    @Autowired
    public TeamMessageController(TeamMessageService teamMessageService) {
        this.teamMessageService = teamMessageService;
    }

    @GetMapping(path = "/teams/{teamId}/team-messages")
    public ResponseEntity<GetTeamMessagesResponse> getTeamMessages(
            @PathVariable("teamId") Long teamId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetTeamMessagesResponse("success", teamMessageService.getTeamMessages(teamId)));
    }
}
