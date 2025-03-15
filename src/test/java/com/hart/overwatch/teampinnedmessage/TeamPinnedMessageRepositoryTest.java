package com.hart.overwatch.teampinnedmessage;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_pinned_message_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamPinnedMessageRepositoryTest {

    @Autowired
    private TeamPinnedMessageRepository teamPinnedMessageRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Team team;

    private List<TeamPinnedMessage> teamPinnedMessages = new ArrayList<>();

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileRepository.save(profileEntity);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
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

    private List<TeamPinnedMessage> createTeamPinnedMessages(User user, Team team, int count) {
        List<TeamPinnedMessage> messages = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            TeamPinnedMessage message = new TeamPinnedMessage();
            message.setTeam(team);
            message.setUser(user);
            message.setIndex(i);
            message.setMessage(String.format("message-%d", i));
            message.setIsEdited(false);
            messages.add(message);
        }
        teamPinnedMessageRepository.saveAll(messages);

        return messages;
    }


    @BeforeEach
    public void setUp() {
        int numOfMessages = 3;
        user = createUser();
        team = createTeam(user);
        teamPinnedMessages = createTeamPinnedMessages(user, team, numOfMessages);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamPinnedMessageRepository.deleteAll();
        teamRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TeamPinnedMessageRepository_TotalTeamPinnedMessages_ReturnLongCount() {
        Long teamId = team.getId();

        long totalTeamPinnedMessages = teamPinnedMessageRepository.totalTeamPinnedMessages(teamId);

        Assertions.assertThat(totalTeamPinnedMessages).isEqualTo(teamPinnedMessages.size());
    }


    @Test
    public void TeamPinnedMessageRepository_GetTeamPinnedMessagesByTeamId_ReturnPage_Of_TeamPinnedMessageDto() {
        Long teamId = team.getId();
        Pageable pageable = PageRequest.of(0, 3);

        Page<TeamPinnedMessageDto> result =
                teamPinnedMessageRepository.getTeamPinnedMessagesByTeamId(teamId, pageable);

        Assertions.assertThat(result.getSize()).isEqualTo(teamPinnedMessages.size());
        List<TeamPinnedMessageDto> messages = result.getContent();

        for (int i = 0; i < messages.size(); i++) {
            TeamPinnedMessageDto message = messages.get(i);
            Assertions.assertThat(message.getUserId())
                    .isEqualTo(teamPinnedMessages.get(i).getUser().getId());
            Assertions.assertThat(message.getIndex())
                    .isEqualTo(teamPinnedMessages.get(i).getIndex());
            Assertions.assertThat(message.getIsEdited())
                    .isEqualTo(teamPinnedMessages.get(i).getIsEdited());
            Assertions.assertThat(message.getMessage())
                    .isEqualTo(teamPinnedMessages.get(i).getMessage());
            Assertions.assertThat(message.getFullName())
                    .isEqualTo(teamPinnedMessages.get(i).getUser().getFullName());
            Assertions.assertThat(message.getAvatarUrl())
                    .isEqualTo(teamPinnedMessages.get(i).getUser().getProfile().getAvatarUrl());
        }

    }
}


