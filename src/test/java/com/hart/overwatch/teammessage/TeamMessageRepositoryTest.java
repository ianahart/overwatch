package com.hart.overwatch.teammessage;

import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_message_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamMessageRepositoryTest {

    @Autowired
    private TeamMessageRepository teamMessageRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Team team;

    private List<TeamMessage> teamMessages;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private Team createTeam(User user) {
        Team teamEntity = new Team();
        teamEntity.setUser(user);
        teamEntity.setTeamName("team one");
        teamRepository.save(teamEntity);

        return teamEntity;
    }

    private List<TeamMessage> createTeamMessages(User user, Team team, int numOfMessages) {
        List<TeamMessage> teamMessages = new ArrayList<>();
        for (int i = 0; i < numOfMessages; i++) {
            TeamMessage teamMessage = new TeamMessage();
            teamMessage.setTeam(team);
            teamMessage.setUser(user);
            teamMessage.setText(String.format("message-%d", i + 1));

            teamMessages.add(teamMessage);
        }

        teamMessageRepository.saveAll(teamMessages);

        return teamMessages;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        int numOfMessages = 3;
        teamMessages = createTeamMessages(user, team, numOfMessages);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamMessageRepository.deleteAll();
        teamRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

}



