package com.hart.overwatch.teampost;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.teamcomment.TeamComment;
import com.hart.overwatch.teampost.dto.TeamPostDto;
import com.hart.overwatch.teampost.request.CreateTeamPostRequest;
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
@WebMvcTest(controllers = TeamPostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamPostService teamPostService;

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

    private List<TeamPost> teamPosts;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url");
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

    private String getCode() {
        return """
                <pre><code>function fetchData(url) {                                           +
                           return data;
                        }
                        """;
    }


    private List<TeamPost> createTeamPosts(User user, Team team, int numOfPosts) {
        List<TeamPost> teamPosts = new ArrayList<>();
        for (int i = 0; i < numOfPosts; i++) {
            TeamPost teamPost = new TeamPost();
            teamPost.setId(Long.valueOf(i + 1));
            teamPost.setTeam(team);
            teamPost.setUser(user);
            teamPost.setLanguage("javascript");
            teamPost.setIsEdited(false);
            teamPost.setCode(getCode());

            teamPosts.add(teamPost);
        }

        return teamPosts;
    }

    private void createTeamComments(User user, List<TeamPost> teamPosts) {
        for (int i = 0; i < teamPosts.size(); i++) {
            TeamComment teamComment = new TeamComment();
            teamComment.setTag(user.getFullName());
            teamComment.setContent("content");
            teamComment.setIsEdited(false);
            teamComment.setTeamPost(teamPosts.get(i));
            teamComment.setUser(user);
            teamComment.setId(Long.valueOf(i + 1));
        }
    }

    private TeamPostDto convertToDto(TeamPost teamPost) {
        TeamPostDto teamPostDto = new TeamPostDto();
        teamPostDto.setId(teamPost.getId());
        teamPostDto.setCode(teamPost.getCode());
        teamPostDto.setTeamId(teamPost.getTeam().getId());
        teamPostDto.setUserId(teamPost.getUser().getId());
        teamPostDto.setFullName(teamPost.getUser().getFullName());
        teamPostDto.setLanguage(teamPost.getLanguage());
        teamPostDto.setIsEdited(teamPost.getIsEdited());
        teamPostDto.setAvatarUrl(teamPost.getUser().getProfile().getAvatarUrl());

        return teamPostDto;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        int numOfPosts = 3;
        teamPosts = createTeamPosts(user, team, numOfPosts);
        createTeamComments(user, teamPosts);
    }



    @Test
    public void TeamPostController_GetTeamPosts_ReturnGetTeamPostsResponse() throws Exception {
        Long teamId = team.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamPostDto TeamPostDto = convertToDto(teamPosts.get(0));
        Page<TeamPostDto> pageResult =
                new PageImpl<>(Collections.singletonList(TeamPostDto), pageable, 1);
        PaginationDto<TeamPostDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(teamPostService.getTeamPosts(teamId, page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/teams/%d/team-posts", teamId))
                        .param("userId", String.valueOf(teamId)).param("page", "0")
                        .param("pageSize", "3").param("direction", "next"));

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
    public void TeamPostController_CreateTeamPost_ReturnCreatTeamPostResponse() throws Exception {
        CreateTeamPostRequest request = new CreateTeamPostRequest();
        request.setCode(getCode());
        request.setUserId(user.getId());
        request.setLanguage("javascript");
        Long teamId = team.getId();

        doNothing().when(teamPostService).createTeamPost(request, teamId);

        ResultActions response =
                mockMvc.perform(post(String.format("/api/v1/teams/%d/team-posts", teamId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TeamPostController_DeleteTeamPost_ReturnDeleteTeamPostResponse() throws Exception {
        Long teamPostId = teamPosts.get(0).getId();

        doNothing().when(teamPostService).deleteTeamPost(teamPostId);
        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/team-posts/%d", teamPostId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }
}


