package com.hart.overwatch.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.team.response.CreateTeamResponse;
import com.hart.overwatch.team.response.GetTeamsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("")
    public ResponseEntity<GetTeamsResponse> getTeams(@RequestParam("userId") Long userId,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTeamsResponse("success",
                teamService.getTeams(userId, page, pageSize, direction)));
    }

    @PostMapping("")
    public ResponseEntity<CreateTeamResponse> createTeam(
            @Valid @RequestBody CreateTeamRequest request) {
        teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTeamResponse("success"));
    }
}
