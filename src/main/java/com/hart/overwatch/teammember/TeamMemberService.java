package com.hart.overwatch.teammember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    private final TeamService teamService;

    private final UserService userService;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository, TeamService teamService,
            UserService userService) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamService = teamService;
        this.userService = userService;
    }

    private boolean alreadyATeamMember(Long teamId, Long userId) {
        return teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
    }

    public void createTeamMember(Long teamId, Long userId) {

        if (alreadyATeamMember(teamId, userId)) {
            throw new BadRequestException("You are already a team member of thsi team");
        }

        Team team = teamService.getTeamByTeamId(teamId);
        User user = userService.getUserById(userId);

        TeamMember teamMember = new TeamMember(team, user);

        teamMemberRepository.save(teamMember);
    }
}
