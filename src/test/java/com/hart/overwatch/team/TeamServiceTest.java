package com.hart.overwatch.team;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.dto.TagDto;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private User user;

    private Team team;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Team createTeam(User user) {
        Team teamEntity = new Team();
        teamEntity.setUser(user);
        teamEntity.setTeamName("team one");
        teamEntity.setTeamDescription("team description");
        teamEntity.setId(1L);

        return teamEntity;
    }

    private TeamDto convertToDto(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setId(team.getId());
        teamDto.setUserId(team.getUser().getId());
        teamDto.setTeamName(team.getTeamName());
        teamDto.setTeamDescription(team.getTeamDescription());
        teamDto.setTotalTeams(1L);

        return teamDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
    }

    @Test
    public void TeamService_CreateTeam_ThrowBadRequestException_MaxTeams() {
        CreateTeamRequest request = new CreateTeamRequest();
        request.setUserId(user.getId());
        request.setTeamName(team.getTeamName());
        request.setTeamDescription(team.getTeamDescription());
        Long MAX_TEAMS = 10L;

        when(teamRepository.getTeamCountByUserId(request.getUserId())).thenReturn(MAX_TEAMS);

        Assertions.assertThatThrownBy(() -> {
            teamService.createTeam(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(String

                .format("You have already added the maximum amount of teams (%d)", MAX_TEAMS));
    }

    @Test
    public void TeamService_CreateTeam_ThrowBadRequestException_AlreadyExists() {
        CreateTeamRequest request = new CreateTeamRequest();
        request.setUserId(user.getId());
        request.setTeamName(team.getTeamName());
        request.setTeamDescription(team.getTeamDescription());

        when(teamRepository.getTeamCountByUserId(request.getUserId())).thenReturn(0L);
        when(teamRepository.existsByUserIdAndTeamName(request.getUserId(), request.getTeamName()))
                .thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            teamService.createTeam(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                String.format("You have already created a team named %s", request.getTeamName()));
    }

    @Test
    public void TeamService_CreateTeam_ReturnNothing() {
        CreateTeamRequest request = new CreateTeamRequest();
        request.setUserId(user.getId());
        request.setTeamName("team two");
        request.setTeamDescription("team description");

        when(teamRepository.getTeamCountByUserId(request.getUserId())).thenReturn(0L);
        when(teamRepository.existsByUserIdAndTeamName(request.getUserId(), request.getTeamName()))
                .thenReturn(false);

        when(userService.getUserById(request.getUserId())).thenReturn(user);

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        teamService.createTeam(request);

        Assertions.assertThatNoException();
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void TeamService_GetTeams_ReturnPaginationOfTeamDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamDto teamDto = convertToDto(team);
        Page<TeamDto> pageResult = new PageImpl<>(Collections.singletonList(teamDto), pageable, 1);
        PaginationDto<TeamDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamRepository.getTeamsByUserId(pageable, user.getId())).thenReturn(pageResult);

        PaginationDto<TeamDto> actualPaginationDto =
                teamService.getTeams(user.getId(), page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto.getItems().size())
                .isEqualTo(expectedPaginationDto.getItems().size());
        Assertions.assertThat(actualPaginationDto.getPage()).isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getTotalPages()).isEqualTo(expectedPaginationDto.getTotalPages());
    }
}


