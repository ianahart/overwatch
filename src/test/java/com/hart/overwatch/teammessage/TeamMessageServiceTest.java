package com.hart.overwatch.teammessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamMessageServiceTest {

    @InjectMocks
    private TeamMessageService teamMessageService;

    @Mock
    private TeamMessageRepository teamMessageRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

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
    public void TeamMessageService_GetTeamMessages_ReturnListOfTeamMessageDto() {
        TeamMessage teamMessage = teamMessages.get(0);
        List<TeamMessageDto> teamMessageDtos = List.of(convertToDto(teamMessage));
        when(teamMessageRepository.getTeamMessagesByTeamId(team.getId()))
                .thenReturn(teamMessageDtos);

        List<TeamMessageDto> result = teamMessageService.getTeamMessages(team.getId());

        Assertions.assertThat(result).hasSize(1);
        TeamMessageDto teamMessageDto = result.get(0);
        Assertions.assertThat(teamMessageDto.getId()).isEqualTo(teamMessage.getId());
        Assertions.assertThat(teamMessageDto.getTeamId()).isEqualTo(teamMessage.getTeam().getId());
        Assertions.assertThat(teamMessageDto.getUserId()).isEqualTo(teamMessage.getUser().getId());
        Assertions.assertThat(teamMessageDto.getText()).isEqualTo(teamMessage.getText());
        Assertions.assertThat(teamMessageDto.getFullName())
                .isEqualTo(teamMessage.getUser().getFullName());
        Assertions.assertThat(teamMessageDto.getAvatarUrl())
                .isEqualTo(teamMessage.getUser().getProfile().getAvatarUrl());
    }

    @Test
    public void TeamMessageService_CreateTeamMessage_ReturnTeamMessageDto() {
        Long teamId = team.getId();
        Long userId = user.getId();
        String text = "<script>alert('XSS')</script> Hello, World!";
        String cleanedText = "Hello, World!";
        String messageJson = String.format("{\"teamId\":%d,\"userId\":%d,\"text\":\"%s\"}", teamId,
                userId, text);


        TeamMessage savedMessage = new TeamMessage(cleanedText, user, team);
        savedMessage.setId(3L);

        TeamMessageDto expectedDto = new TeamMessageDto(savedMessage.getId(),
                savedMessage.getText(), savedMessage.getCreatedAt(), user.getId(),
                user.getFullName(), null, team.getId());

        when(userService.getUserById(userId)).thenReturn(user);
        when(teamService.getTeamByTeamId(teamId)).thenReturn(team);
        when(teamMessageRepository.save(any(TeamMessage.class))).thenAnswer(invocation -> {
            TeamMessage argument = invocation.getArgument(0);
            argument.setId(3L);
            return argument;
        });

        TeamMessageDto actualDto = teamMessageService.createTeamMessage(messageJson);

        Assertions.assertThat(actualDto).isNotNull();
        Assertions.assertThat(actualDto.getId()).isEqualTo(expectedDto.getId());
        Assertions.assertThat(actualDto.getText()).isEqualTo(expectedDto.getText());
        Assertions.assertThat(actualDto.getUserId()).isEqualTo(expectedDto.getUserId());
        Assertions.assertThat(actualDto.getFullName()).isEqualTo(expectedDto.getFullName());
        Assertions.assertThat(actualDto.getTeamId()).isEqualTo(expectedDto.getTeamId());

        verify(teamMessageRepository, times(1)).save(any(TeamMessage.class));
    }
}


