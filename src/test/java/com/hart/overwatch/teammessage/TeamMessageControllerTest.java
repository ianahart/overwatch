package com.hart.overwatch.teammessage;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamMessageService teamMessageService;

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

    private List<TeamMessage> teamMessages;

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

    private List<TeamMessage> createTeamMessages(User user, Team team, int numOfMessages) {
        List<TeamMessage> teamMessages = new ArrayList<>();
        for (int i = 0; i < numOfMessages; i++) {
            TeamMessage teamMessage = new TeamMessage();
            teamMessage.setId(Long.valueOf(i + 1));
            teamMessage.setTeam(team);
            teamMessage.setUser(user);
            teamMessage.setText(String.format("message-%d", i + 1));

            teamMessages.add(teamMessage);
        }
        return teamMessages;
    }

    private TeamMessageDto convertToDto(TeamMessage teamMessage) {
        TeamMessageDto teamMessageDto = new TeamMessageDto();
        teamMessageDto.setId(teamMessage.getId());
        teamMessageDto.setText(teamMessage.getText());
        teamMessageDto.setUserId(teamMessage.getUser().getId());
        teamMessageDto.setFullName(teamMessage.getUser().getFullName());
        teamMessageDto.setAvatarUrl(teamMessage.getUser().getProfile().getAvatarUrl());
        teamMessageDto.setTeamId(teamMessage.getTeam().getId());

        return teamMessageDto;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        int numOfMessages = 3;
        teamMessages = createTeamMessages(user, team, numOfMessages);
    }

    @Test
    public void TeamMessageController_GetTeamMessages_ReturnGetTeamMessagesResponse()
            throws Exception {
        List<TeamMessageDto> teamMessageDtos =
                teamMessages.stream().map(this::convertToDto).toList();

        when(teamMessageService.getTeamMessages(team.getId())).thenReturn(teamMessageDtos);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/teams/%d/team-messages", team.getId())));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                        Matchers.hasSize(teamMessages.size())));
    }


}


