package com.hart.overwatch.teammember;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;
import com.hart.overwatch.teammember.response.GetTeamMemberTeamsResponse;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;

@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    private final TeamService teamService;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository, TeamService teamService,
            UserService userService, PaginationService paginationService) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamService = teamService;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private TeamMember getTeamMemberById(Long teamMemberId) {
        return teamMemberRepository.findById(teamMemberId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find a team member with the id %d", teamMemberId)));
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

    public GetTeamMemberTeamsResponse getTeamMemberTeams(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<TeamMemberTeamDto> result =
                this.teamMemberRepository.getTeamsByTeamMember(pageable, userId);

        return new GetTeamMemberTeamsResponse("success",
                new PaginationDto<TeamMemberTeamDto>(result.getContent(), result.getNumber(),
                        pageSize, result.getTotalPages(), direction, result.getTotalElements()),
                countTeamMemberTeams(userId));
    }

    private long countTeamMemberTeams(Long userId) {
        if (userId == null) {
            return 0;
        }
        return teamMemberRepository.countTeamMemberTeams(userId);
    }

    public void deleteTeamMember(Long teamMemberId) {

        User user = userService.getCurrentlyLoggedInUser();
        TeamMember teamMember = getTeamMemberById(teamMemberId);

        if (!user.getId().equals(teamMember.getUser().getId())) {
            throw new ForbiddenException(
                    "Cannot perform the action of deleting a team member from a team");
        }
        teamMemberRepository.delete(teamMember);
    }

    public List<Long> getTeamMemberIds(Team team) {
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMemberByTeamId(team.getId());
        if (teamMembers.isEmpty()) {
            return null;
        }
        return teamMembers.stream().filter(teamMember -> teamMember.getUser().getLoggedIn())
                .map(teamMember -> teamMember.getUser().getId()).collect(Collectors.toList());
    }



}
