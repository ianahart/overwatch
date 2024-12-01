package com.hart.overwatch.team;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.hamcrest.CoreMatchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

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
    public void TeamController_GetTeams_ReturnGetTeamsResponse() throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamDto teamDto = convertToDto(team);
        Page<TeamDto> pageResult = new PageImpl<>(Collections.singletonList(teamDto), pageable, 1);

        PaginationDto<TeamDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(teamService.getTeams(user.getId(), page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/teams").param("page", "0")
                .param("userId", "1").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(teamDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].teamName",
                        CoreMatchers.is(teamDto.getTeamName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].userId",
                        CoreMatchers.is(teamDto.getUserId().intValue())));
    }



    @Test
    public void TeamCommentControllerTest_CreateTeam_ReturnCreateTeamResponse() throws Exception {

        CreateTeamRequest request = new CreateTeamRequest();
        request.setUserId(user.getId());
        request.setTeamName("team two");
        request.setTeamDescription("team two description");
        doNothing().when(teamService).createTeam(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/teams").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

}


