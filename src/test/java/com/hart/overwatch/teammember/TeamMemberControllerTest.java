package com.hart.overwatch.teammember;

import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.teammember.dto.TeamMemberDto;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;
import com.hart.overwatch.teammember.response.GetTeamMemberTeamsResponse;
import com.hart.overwatch.teammember.response.GetTeamMembersResponse;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamMemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMemberService teamMemberService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void TeamMemberController_GetTeamMemberTeams_ReturnGetTeamMemberTeamsResponse()
            throws Exception {
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
        GetTeamMemberTeamsResponse getTeamMemberTeamsResponse = new GetTeamMemberTeamsResponse();
        getTeamMemberTeamsResponse.setData(expectedPaginationDto);
        getTeamMemberTeamsResponse.setMessage("success");
        getTeamMemberTeamsResponse.setTotalTeamMemberTeams(1L);
        when(teamMemberService.getTeamMemberTeams(user.getId(), page, pageSize, direction))
                .thenReturn(getTeamMemberTeamsResponse);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/team-members/%d/teams", user.getId()))
                        .param("page", "0").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));
    }

    @Test
    public void TeamMemberController_GetTeamMembers_ReturnGetTeamMembersResponse()
            throws Exception {
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
        GetTeamMembersResponse getTeamMembersResponse = new GetTeamMembersResponse();
        getTeamMembersResponse.setData(expectedPaginationDto);
        getTeamMembersResponse.setMessage("success");
        getTeamMembersResponse.setAdmin(new TeamMemberDto());

        when(teamMemberService.getTeamMembers(team.getId(), page, pageSize, direction))
                .thenReturn(getTeamMembersResponse);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/teams/%d/team-members", team.getId()))
                        .param("page", "0").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));
    }

    @Test
    public void TeamMemberController_searchTeamMembers_ReturnSearchTeamMembersResponse()
            throws Exception {
        int page = 0;
        int pageSize = 3;
        String search = "John Doe";
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamMemberDto teamMemberDto = convertToTeamMemberDto(teamMember);
        Page<TeamMemberDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamMemberDto), pageable, 1);
        PaginationDto<TeamMemberDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(teamMemberService.searchTeamMembers(team.getId(), page, pageSize, direction, search))
                .thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/teams/%d/team-members/search", team.getId()))
                        .param("page", "0").param("pageSize", "3").param("direction", "next")
                        .param("search", search));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));
    }

    @Test
    public void TeamMemberController_DeleteTeamMember_ReturnDeleteTeamMemberResponse()
            throws Exception {
        Long teamMemberId = teamMember.getId();

        doNothing().when(teamMemberService).deleteTeamMember(teamMemberId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/team-members/%d", teamMemberId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }


}


