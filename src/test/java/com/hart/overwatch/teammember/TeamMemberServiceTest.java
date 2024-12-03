package com.hart.overwatch.teammember;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.Optional;
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
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teammember.dto.TeamMemberDto;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;
import com.hart.overwatch.teammember.response.GetTeamMemberTeamsResponse;
import com.hart.overwatch.teammember.response.GetTeamMembersResponse;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamMemberServiceTest {

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private User user;

    private Team team;

    private TeamMember teamMember;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(1L);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);

        return userEntity;
    }

    private Team createTeam(User user) {
        Team teamEntity = new Team();
        teamEntity.setUser(user);
        teamEntity.setTeamName("team one");
        teamEntity.setId(1L);

        return teamEntity;
    }

    private TeamMember createTeamMember(User user, Team team) {
        TeamMember teamMemberEntity = new TeamMember();
        teamMemberEntity.setUser(user);
        teamMemberEntity.setTeam(team);
        teamMemberEntity.setId(1L);

        return teamMemberEntity;
    }

    private TeamMemberTeamDto convertToTeamMemberTeamDto(TeamMember teamMember) {
        TeamMemberTeamDto teamMemberTeamDto = new TeamMemberTeamDto();
        teamMemberTeamDto.setId(teamMember.getId());
        teamMemberTeamDto.setTeamId(teamMember.getTeam().getId());
        teamMemberTeamDto.setUserId(teamMember.getUser().getId());

        return teamMemberTeamDto;
    }

    private TeamMemberDto convertToTeamMemberDto(TeamMember teamMember) {
        TeamMemberDto teamMemberDto = new TeamMemberDto();
        teamMemberDto.setId(teamMember.getId());
        teamMemberDto.setTeamId(teamMember.getTeam().getId());
        teamMemberDto.setUserId(teamMember.getUser().getId());
        teamMemberDto.setFullName(teamMember.getUser().getFullName());
        teamMemberDto.setProfileId(teamMember.getUser().getProfile().getId());
        teamMemberDto.setAvatarUrl(teamMember.getUser().getProfile().getAvatarUrl());

        return teamMemberDto;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        teamMember = createTeamMember(user, team);
    }

    @Test
    public void TeamMemberService_CreatTeamMember_ThrowBadRequestException() {
        Long userId = user.getId();
        Long teamId = team.getId();

        when(teamMemberRepository.existsByTeamIdAndUserId(teamId, userId)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            teamMemberService.createTeamMember(teamId, userId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You are already a team member of this team");
    }

    @Test
    public void TeamMemberService_CreateTeamMember_ReturnNothing() {
        User newUser = new User();
        newUser.setId(2L);
        Team newTeam = new Team();
        newTeam.setId(2L);

        when(teamMemberRepository.existsByTeamIdAndUserId(newTeam.getId(), newUser.getId()))
                .thenReturn(false);
        when(teamService.getTeamByTeamId(newTeam.getId())).thenReturn(newTeam);
        when(userService.getUserById(newUser.getId())).thenReturn(newUser);

        TeamMember newTeamMember = new TeamMember(newTeam, newUser);
        when(teamMemberRepository.save(any(TeamMember.class))).thenReturn(newTeamMember);

        teamMemberService.createTeamMember(newTeam.getId(), newUser.getId());

        Assertions.assertThatNoException();
        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
    }

    @Test
    public void TeamMemberService_GetTeamMemberTeams_ReturnGetTeamMemberTeamsResponse() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamMemberTeamDto teamMemberTeamDto = convertToTeamMemberTeamDto(teamMember);
        Page<TeamMemberTeamDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamMemberTeamDto), pageable, 1);
        PaginationDto<TeamMemberTeamDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);

        when(teamMemberRepository.getTeamsByTeamMember(pageable, user.getId()))
                .thenReturn(pageResult);

        when(teamMemberRepository.countTeamMemberTeams(user.getId())).thenReturn(1L);

        GetTeamMemberTeamsResponse result =
                teamMemberService.getTeamMemberTeams(user.getId(), page, pageSize, direction);

        Assertions.assertThat(result.getMessage()).isEqualTo("success");
        Assertions.assertThat(result.getTotalTeamMemberTeams()).isEqualTo(1L);
        Assertions.assertThat(result.getData()).isNotNull();
        PaginationDto<TeamMemberTeamDto> actualPaginationDto = result.getData();
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        TeamMemberTeamDto actualTeamMeamberTeamDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamMeamberTeamDto.getId())
                .isEqualTo(teamMemberTeamDto.getId());
        Assertions.assertThat(actualTeamMeamberTeamDto.getTeamId())
                .isEqualTo(teamMemberTeamDto.getTeamId());
        Assertions.assertThat(actualTeamMeamberTeamDto.getUserId())
                .isEqualTo(teamMemberTeamDto.getUserId());
        Assertions.assertThat(actualTeamMeamberTeamDto.getTeamName())
                .isEqualTo(teamMemberTeamDto.getTeamName());
    }

    @Test
    public void TeamMemberService_DeleteTeamMember_ThrowForbiddenException() {
        Long teamMemberId = teamMember.getId();
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(teamMemberRepository.findById(teamMemberId)).thenReturn(Optional.of(teamMember));

        Assertions.assertThatThrownBy(() -> {
            teamMemberService.deleteTeamMember(teamMemberId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot perform the action of deleting a team member from a team");
    }

    @Test
    public void TeamMemberService_DeleteTeamMember_ReturnNothing() {
        Long teamMemberId = teamMember.getId();

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(teamMemberRepository.findById(teamMemberId)).thenReturn(Optional.of(teamMember));

        doNothing().when(teamMemberRepository).delete(teamMember);

        teamMemberService.deleteTeamMember(teamMemberId);

        Assertions.assertThatNoException();
        verify(teamMemberRepository, times(1)).delete(teamMember);
    }

    @Test
    public void TeamMemberService_GetTeamMembers_ReturnGetTeamMembersResponse() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamMemberDto teamMemberDto = convertToTeamMemberDto(teamMember);
        Page<TeamMemberDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamMemberDto), pageable, 1);
        PaginationDto<TeamMemberDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamMemberRepository.getTeamMembersByTeamId(pageable, teamMember.getTeam().getId()))
                .thenReturn(pageResult);
        when(teamService.getTeamByTeamId(teamMember.getTeam().getId())).thenReturn(team);

        GetTeamMembersResponse response = teamMemberService
                .getTeamMembers(teamMember.getTeam().getId(), page, pageSize, direction);

        Assertions.assertThat(response.getMessage()).isEqualTo("success");
        Assertions.assertThat(response.getData()).isNotNull();
        PaginationDto<TeamMemberDto> actualPaginationDto = response.getData();
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        TeamMemberDto actualTeamMeamberDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamMeamberDto.getId()).isEqualTo(teamMemberDto.getId());
        Assertions.assertThat(actualTeamMeamberDto.getTeamId())
                .isEqualTo(teamMemberDto.getTeamId());
        Assertions.assertThat(actualTeamMeamberDto.getUserId())
                .isEqualTo(teamMemberDto.getUserId());
        Assertions.assertThat(actualTeamMeamberDto.getProfileId())
                .isEqualTo(teamMemberDto.getProfileId());
        Assertions.assertThat(actualTeamMeamberDto.getFullName())
                .isEqualTo(teamMemberDto.getFullName());
        Assertions.assertThat(actualTeamMeamberDto.getAvatarUrl())
                .isEqualTo(teamMemberDto.getAvatarUrl());
    }

        @Test
    public void TeamMemberService_SearchTeamMembers_ReturnPaginationDtoOfTeamMemberDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String search = "john doe";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamMemberDto teamMemberDto = convertToTeamMemberDto(teamMember);
        Page<TeamMemberDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamMemberDto), pageable, 1);
        PaginationDto<TeamMemberDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamMemberRepository.searchTeamMembersByTeamId(pageable, team.getId(), search))
                .thenReturn(pageResult);

        PaginationDto<TeamMemberDto> actualPaginationDto = teamMemberService
                .searchTeamMembers(team.getId(), page, pageSize, direction, search);
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        TeamMemberDto actualTeamMeamberDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamMeamberDto.getId()).isEqualTo(teamMemberDto.getId());
        Assertions.assertThat(actualTeamMeamberDto.getTeamId())
                .isEqualTo(teamMemberDto.getTeamId());
        Assertions.assertThat(actualTeamMeamberDto.getUserId())
                .isEqualTo(teamMemberDto.getUserId());
        Assertions.assertThat(actualTeamMeamberDto.getProfileId())
                .isEqualTo(teamMemberDto.getProfileId());
        Assertions.assertThat(actualTeamMeamberDto.getFullName())
                .isEqualTo(teamMemberDto.getFullName());
        Assertions.assertThat(actualTeamMeamberDto.getAvatarUrl())
                .isEqualTo(teamMemberDto.getAvatarUrl());
    }
}


