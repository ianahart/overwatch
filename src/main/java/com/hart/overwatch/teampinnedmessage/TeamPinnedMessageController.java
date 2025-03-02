package com.hart.overwatch.teampinnedmessage;

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
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.request.ReorderTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.request.UpdateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.response.CreateTeamPinnedMessageResponse;
import com.hart.overwatch.teampinnedmessage.response.DeleteTeamPinnedMessageResponse;
import com.hart.overwatch.teampinnedmessage.response.GetTeamPinnedMessagesResponse;
import com.hart.overwatch.teampinnedmessage.response.ReorderTeamPinnedMessageResponse;
import com.hart.overwatch.teampinnedmessage.response.UpdateTeamPinnedMessageResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamPinnedMessageController {

    private final TeamPinnedMessageService teamPinnedMessageService;

    @Autowired
    public TeamPinnedMessageController(TeamPinnedMessageService teamPinnedMessageService) {
        this.teamPinnedMessageService = teamPinnedMessageService;
    }

    @GetMapping(path = "/{teamId}/team-pinned-messages")
    public ResponseEntity<GetTeamPinnedMessagesResponse> getTeamPinnedMessages(
            @PathVariable("teamId") Long teamId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTeamPinnedMessagesResponse(
                "success", teamPinnedMessageService.getTeamPinnedMessages(teamId)));
    }

    @PostMapping(path = "/{teamId}/team-pinned-messages")
    public ResponseEntity<CreateTeamPinnedMessageResponse> createTeamPinnedMessage(
            @PathVariable("teamId") Long teamId,
            @Valid @RequestBody CreateTeamPinnedMessageRequest request) {
        teamPinnedMessageService.createTeamPinnedMessage(teamId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateTeamPinnedMessageResponse("success"));
    }

    @PatchMapping(path = "/{teamId}/team-pinned-messages/{teamPinnedMessageId}")
    public ResponseEntity<UpdateTeamPinnedMessageResponse> updateTeamPinnedMessage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("teamPinnedMessageId") Long teamPinnedMessageId,
            @Valid @RequestBody UpdateTeamPinnedMessageRequest request) {
        teamPinnedMessageService.updateTeamPinnedMessage(teamId, teamPinnedMessageId, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateTeamPinnedMessageResponse("success"));
    }

    @DeleteMapping(path = "/{teamId}/team-pinned-messages/{teamPinnedMessageId}")
    public ResponseEntity<DeleteTeamPinnedMessageResponse> deleteTeamPinnedMessage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("teamPinnedMessageId") Long teamPinnedMessageId

    ) {
        teamPinnedMessageService.deleteTeamPinnedMessage(teamId, teamPinnedMessageId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteTeamPinnedMessageResponse("success"));

    }

    @PostMapping(path = "/{teamId}/team-pinned-messages/reorder")
    public ResponseEntity<ReorderTeamPinnedMessageResponse> reorderTeamPinnedMessages(
            @PathVariable("teamId") Long teamId,
            @RequestBody ReorderTeamPinnedMessageRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ReorderTeamPinnedMessageResponse("success", teamPinnedMessageService
                        .reorderTeamPinnedMessages(request.getTeamPinnedMessages(), teamId)));
    }
}
