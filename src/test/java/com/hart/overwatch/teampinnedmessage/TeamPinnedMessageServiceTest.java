package com.hart.overwatch.teampinnedmessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamPinnedMessageServiceTest {

    @InjectMocks
    private TeamPinnedMessageService teamPinnedMessageService;

    @Mock
    private TeamPinnedMessageRepository teamPinnedMessageRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

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


    @BeforeEach
    public void setUp() {
        int numOfMessages = 3;
        user = createUser();
        team = createTeam(user);
        teamPinnedMessages = createTeamPinnedMessages(user, team, numOfMessages);

    }

    @Test
    public void TeamPinnedMessageService_GetTeamPinnedMessageById_ThrowsNotFoundException() {
        Long teamPinnedMessageId = 999L;

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.getTeamPinnedMessageById(teamPinnedMessageId);
        }).isInstanceOf(NotFoundException.class).hasMessage(String
                .format("Could not find team pinned message with the id %d", teamPinnedMessageId));

    }

    @Test
    public void TeamPinnedMessageService_GetTeamPinnedMessageById_ReturnTeamPinnedMessage() {
        TeamPinnedMessage teamPinnedMessage = teamPinnedMessages.get(0);

        when(teamPinnedMessageRepository.findById(teamPinnedMessage.getId()))
                .thenReturn(Optional.of(teamPinnedMessage));

        TeamPinnedMessage returnedTeamPinnedMessage =
                teamPinnedMessageService.getTeamPinnedMessageById(teamPinnedMessage.getId());

        Assertions.assertThat(returnedTeamPinnedMessage).isNotNull();
        Assertions.assertThat(returnedTeamPinnedMessage.getId())
                .isEqualTo(teamPinnedMessage.getId());
    }

    @Test
    public void teamPinnedMessageService_CreateTeamPinnedMessage_Throw_BadRequestException() {
        CreateTeamPinnedMessageRequest request = new CreateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message 4");
        long MAX_TEAM_PINNED_MESSAGES = 5L;
        when(teamPinnedMessageRepository.totalTeamPinnedMessages(team.getId()))
                .thenReturn(MAX_TEAM_PINNED_MESSAGES + 1);

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.createTeamPinnedMessage(team.getId(), null);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                String.format("You have exceeded the maximum amount of admin messages %d",
                        MAX_TEAM_PINNED_MESSAGES));
    }

}


