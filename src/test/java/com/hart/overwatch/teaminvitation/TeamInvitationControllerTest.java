package com.hart.overwatch.teaminvitation;

import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;
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
import org.hamcrest.CoreMatchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = TeamInvitationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamInvitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamInvitationService teamInvitationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User sender;

    private User receiver;

    private Team team;

    private TeamInvitation teamInvitation;


    private User createSender() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(1L);
        User senderEntity = new User("jane@mail.com", "Jane", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        senderEntity.setId(1L);

        return senderEntity;
    }


    private User createReceiver() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(2L);
        User receiverEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        receiverEntity.setId(2L);

        return receiverEntity;
    }

    private Team createTeam(User sender) {
        Team teamEntity = new Team();
        teamEntity.setUser(sender);
        teamEntity.setTeamName("team one");
        teamEntity.setId(1L);

        return teamEntity;
    }

    private TeamInvitation createTeamInvitation(User sender, User receiver, Team team) {
        TeamInvitation teamInvitationEntity = new TeamInvitation();
        teamInvitationEntity.setTeam(team);
        teamInvitationEntity.setSender(sender);
        teamInvitationEntity.setReceiver(receiver);
        teamInvitationEntity.setStatus(InvitationStatus.PENDING);
        teamInvitationEntity.setId(1L);

        return teamInvitationEntity;
    }

    private TeamInvitationDto convertToDto(TeamInvitation teamInvitation) {
        TeamInvitationDto teamInvitationDto = new TeamInvitationDto();
        teamInvitationDto.setId(teamInvitation.getId());
        teamInvitationDto.setTeamId(teamInvitation.getTeam().getId());
        teamInvitationDto.setSenderId(teamInvitation.getSender().getId());
        teamInvitationDto.setTeamName(teamInvitation.getTeam().getTeamName());
        teamInvitationDto.setReceiverId(teamInvitation.getReceiver().getId());
        teamInvitationDto.setStatus(teamInvitation.getStatus());
        teamInvitationDto.setSenderFullName(teamInvitation.getSender().getFullName());
        teamInvitationDto
                .setSenderAvatarUrl(teamInvitation.getSender().getProfile().getAvatarUrl());

        return teamInvitationDto;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();
        team = createTeam(sender);
        teamInvitation = createTeamInvitation(sender, receiver, team);
    }


}


