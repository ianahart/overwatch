package com.hart.overwatch.teamcomment;

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
import com.hart.overwatch.teamcomment.dto.MinTeamCommentDto;
import com.hart.overwatch.teamcomment.dto.TeamCommentDto;
import com.hart.overwatch.teamcomment.request.CreateTeamCommentRequest;
import com.hart.overwatch.teampost.TeamPost;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamCommentService teamCommentService;

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

    private TeamPost teamPost;

    private List<TeamComment> teamComments;

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


    private TeamPost createTeamPost(User user, Team team) {
        TeamPost teamPostEntity = new TeamPost();
        teamPostEntity.setTeam(team);
        teamPostEntity.setUser(user);
        teamPostEntity.setLanguage("javascript");
        teamPostEntity.setIsEdited(false);
        teamPostEntity.setCode(getCode());
        teamPostEntity.setId(1L);

        return teamPostEntity;
    }

    private List<TeamComment> createTeamComments(User user, TeamPost teamPost, int numOfComments) {
        List<TeamComment> teamCommentEntities = new ArrayList<>();
        for (int i = 0; i < numOfComments; i++) {
            TeamComment teamCommentEntity = new TeamComment();
            teamCommentEntity.setId(Long.valueOf(i + 1));
            teamCommentEntity.setTag(user.getFullName());
            teamCommentEntity.setUser(user);
            teamCommentEntity.setContent("content");
            teamCommentEntity.setIsEdited(false);
            teamCommentEntity.setTeamPost(teamPost);

            teamCommentEntities.add(teamCommentEntity);
        }
        return teamCommentEntities;
    }

    private TeamCommentDto convertToDto(TeamComment teamComment) {
        TeamCommentDto teamCommentDto = new TeamCommentDto();
        teamCommentDto.setId(teamComment.getId());
        teamCommentDto.setTag(teamComment.getTag());
        teamCommentDto.setUserId(teamComment.getUser().getId());
        teamCommentDto.setContent(teamComment.getContent());
        teamCommentDto.setFullName(teamComment.getUser().getFullName());
        teamCommentDto.setIsEdited(teamComment.getIsEdited());
        teamCommentDto.setAvatarUrl(teamComment.getUser().getProfile().getAvatarUrl());
        teamCommentDto.setTeamPostId(teamComment.getTeamPost().getId());

        return teamCommentDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        teamPost = createTeamPost(user, team);
        int numOfComments = 3;
        teamComments = createTeamComments(user, teamPost, numOfComments);
    }

    @Test
    public void TeamCommentController_CreateTeamComment_ReturnCreateTeamCommentResponse()
            throws Exception {
        Long teamPostId = teamPost.getId();
        CreateTeamCommentRequest request = new CreateTeamCommentRequest();
        request.setTag(user.getFullName());
        request.setUserId(user.getId());
        request.setContent("comment content");

        doNothing().when(teamCommentService).createTeamComment(request, teamPostId);

        ResultActions response = mockMvc
                .perform(post(String.format("/api/v1/team-posts/%d/team-comments", teamPostId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TeamCommentController_GetTeamComments_ReturnGetTeamCommentsResponse()
            throws Exception {
        Long teamPostId = teamPost.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamCommentDto teamCommentDto = convertToDto(teamComments.get(0));
        Page<TeamCommentDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamCommentDto), pageable, 1);
        PaginationDto<TeamCommentDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(teamCommentService.getTeamComments(teamPostId, page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/team-posts/%d/team-comments", teamPostId))
                        .param("userId", String.valueOf(teamPostId)).param("page", "0")
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
    public void TeamCommentController_GetTeamComment_ReturnGetTeamCommentResponse()
            throws Exception {
        TeamComment teamComment = teamComments.get(0);
        MinTeamCommentDto teamCommentDto =
                new MinTeamCommentDto(teamComment.getContent(), teamComment.getTag());

        when(teamCommentService.getTeamComment(teamComment.getId())).thenReturn(teamCommentDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/team-posts/%d/team-comments/%d",
                        teamPost.getId(), teamComment.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tag",
                        CoreMatchers.is(teamComment.getTag())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content",
                        CoreMatchers.is(teamComment.getContent())));

    }

}


