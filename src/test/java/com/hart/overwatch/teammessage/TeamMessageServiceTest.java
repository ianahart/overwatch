package com.hart.overwatch.teammessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
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

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        int numOfMessages = 3;
        teamMessages = createTeamMessages(user, team, numOfMessages);
    }


}


