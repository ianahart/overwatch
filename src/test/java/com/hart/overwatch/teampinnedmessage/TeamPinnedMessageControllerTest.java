package com.hart.overwatch.teampinnedmessage;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.request.UpdateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.response.UpdateTeamPinnedMessageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamPinnedMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamPinnedMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamPinnedMessageService teamPinnedMessageService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Team team;

    private List<TeamPinnedMessage> teamPinnedMessages = new ArrayList<>();

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

    private List<TeamPinnedMessage> createTeamPinnedMessages(User user, Team team, int count) {
        List<TeamPinnedMessage> messages = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            TeamPinnedMessage message = new TeamPinnedMessage();
            message.setId(Long.valueOf(i + 1));
            message.setTeam(team);
            message.setUser(user);
            message.setIndex(i);
            message.setMessage(String.format("message-%d", i));
            message.setIsEdited(false);
            messages.add(message);
        }

        return messages;
    }


    private TeamPinnedMessageDto convertToDto(TeamPinnedMessage message) {
        TeamPinnedMessageDto dto = new TeamPinnedMessageDto();
        dto.setId(message.getId());
        dto.setUserId(message.getUser().getId());
        dto.setIndex(message.getIndex());
        dto.setMessage(message.getMessage());
        dto.setFullName(message.getUser().getFullName());
        dto.setIsEdited(message.getIsEdited());
        dto.setAvatarUrl(message.getUser().getProfile().getAvatarUrl());

        return dto;
    }


    @BeforeEach
    public void setUp() {
        int numOfMessages = 3;
        user = createUser();
        team = createTeam(user);
        teamPinnedMessages = createTeamPinnedMessages(user, team, numOfMessages);

    }

    @Test
    public void TeamPinnedMessageController_GetTeamPinnedMessages_ReturnGetTeamPinnedMessagesResponse()
            throws Exception {
        Long teamId = team.getId();
        TeamPinnedMessageDto teamPinnedMessageDto = convertToDto(teamPinnedMessages.get(0));

        when(teamPinnedMessageService.getTeamPinnedMessages(teamId))
                .thenReturn(List.of(teamPinnedMessageDto));

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/teams/%d/team-pinned-messages", teamId))
                        .param("userId", String.valueOf(teamId)).param("page", "0")
                        .param("pageSize", "3").param("direction", "next"));


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                        Matchers.hasSize(Math.toIntExact(1L))));
    }

    @Test
    public void TeamPinnedMessageController_CreateTeamPinnedMessage_ReturnCreateTeamPinnedMessageResponse()
            throws Exception {
        Long teamId = team.getId();
        CreateTeamPinnedMessageRequest request = new CreateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message 4");

        doNothing().when(teamPinnedMessageService).createTeamPinnedMessage(teamId, request);

        ResultActions response =
                mockMvc.perform(post(String.format("/api/v1/teams/%d/team-pinned-messages", teamId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void TeamPinnedMessageController_UpdateTeamPinnedMessage_ReturnUpdateTeamPinnedMessageResponse()
            throws Exception {
        UpdateTeamPinnedMessageRequest request = new UpdateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message edited");

        doNothing().when(teamPinnedMessageService).updateTeamPinnedMessage(team.getId(),
                teamPinnedMessages.get(0).getId(), request);

        ResultActions response = mockMvc.perform(
                patch(String.format("/api/v1/teams/%d/team-pinned-messages/%d", team.getId(),
                        teamPinnedMessages.get(0).getId())).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

}


