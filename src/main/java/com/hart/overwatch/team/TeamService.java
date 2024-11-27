package com.hart.overwatch.team;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class TeamService {

    private final Long MAX_TEAMS = 10L;

    private final TeamRepository teamRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserService userService,
            PaginationService paginationService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    public Team getTeamByTeamId(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find team with id %d", teamId)));
    }


    private boolean canCreateTeam(Long userId, String teamName) {
        return !(teamRepository.existsByUserIdAndTeamName(userId, teamName));
    }

    public void createTeam(CreateTeamRequest request) {
        String teamName = Jsoup.clean(request.getTeamName(), Safelist.none());
        String teamDescription = Jsoup.clean(request.getTeamDescription(), Safelist.none());
        Long userId = request.getUserId();

        if (teamRepository.getTeamCountByUserId(userId) >= MAX_TEAMS) {
            throw new BadRequestException(String
                    .format("You have already added the maximum amount of teams (%d)", MAX_TEAMS));
        }

        if (!canCreateTeam(userId, teamName.toLowerCase())) {
            throw new BadRequestException(
                    String.format("You have already created a team named %s", teamName));
        }

        User user = userService.getUserById(userId);

        Team team = new Team(teamName, teamDescription, user);

        teamRepository.save(team);
    }


    public PaginationDto<TeamDto> getTeams(Long userId, int page, int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<TeamDto> result = teamRepository.getTeamsByUserId(pageable, userId);

        return new PaginationDto<TeamDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }
}
