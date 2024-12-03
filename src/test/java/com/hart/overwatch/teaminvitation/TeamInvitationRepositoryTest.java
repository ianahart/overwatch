package com.hart.overwatch.teaminvitation;

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
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_invitation_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamInvitationRepositoryTest {

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User sender;

    private User receiver;

    private Team team;

    private TeamInvitation teamInvitation;


    private User createSender() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileRepository.save(profileEntity);
        User senderEntity = new User("jane@mail.com", "Jane", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        userRepository.save(senderEntity);

        return senderEntity;
    }


    private User createReceiver() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileRepository.save(profileEntity);
        User receiverEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        userRepository.save(receiverEntity);

        return receiverEntity;
    }

    private Team createTeam(User sender) {
        Team teamEntity = new Team();
        teamEntity.setUser(sender);
        teamEntity.setTeamName("team one");
        teamRepository.save(teamEntity);

        return teamEntity;
    }

    private TeamInvitation createTeamInvitation(User sender, User receiver, Team team) {
        TeamInvitation teamInvitationEntity = new TeamInvitation();
        teamInvitationEntity.setTeam(team);
        teamInvitationEntity.setSender(sender);
        teamInvitationEntity.setReceiver(receiver);
        teamInvitationEntity.setStatus(InvitationStatus.PENDING);
        teamInvitationRepository.save(teamInvitationEntity);

        return teamInvitationEntity;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();
        team = createTeam(sender);
        teamInvitation = createTeamInvitation(sender, receiver, team);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamInvitationRepository.deleteAll();
        teamRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TeamInvitationRepository_GetTeamInvitationsByReceiverId_ReturnPageOfTeamInvitationDto() {
        Long receiverId = receiver.getId();
        int page = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<TeamInvitationDto> result =
                teamInvitationRepository.getTeamInvitationsByReceiverId(pageable, receiverId);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        TeamInvitationDto teamInvitationDto = result.getContent().get(0);
        Assertions.assertThat(teamInvitationDto.getId()).isEqualTo(teamInvitation.getId());
        Assertions.assertThat(teamInvitationDto.getTeamId())
                .isEqualTo(teamInvitation.getTeam().getId());
        Assertions.assertThat(teamInvitationDto.getSenderId())
                .isEqualTo(teamInvitation.getSender().getId());
        Assertions.assertThat(teamInvitationDto.getReceiverId())
                .isEqualTo(teamInvitation.getReceiver().getId());
        Assertions.assertThat(teamInvitationDto.getTeamName())
                .isEqualTo(teamInvitation.getTeam().getTeamName());
        Assertions.assertThat(teamInvitationDto.getSenderFullName())
                .isEqualTo(teamInvitation.getSender().getFullName());
        Assertions.assertThat(teamInvitationDto.getSenderAvatarUrl())
                .isEqualTo(teamInvitation.getSender().getProfile().getAvatarUrl());
        Assertions.assertThat(teamInvitationDto.getStatus()).isEqualTo(teamInvitation.getStatus());
    }

    @Test
    public void TeamInvitationRepository_ExistsByReceiverIdAndSenderIdAndTeamId_ReturnBooleanTrue() {
        Long receiverId = receiver.getId();
        Long senderId = sender.getId();
        Long teamId = team.getId();

        boolean exists = teamInvitationRepository.existsByReceiverIdAndSenderIdAndTeamId(receiverId,
                senderId, teamId);

        Assertions.assertThat(exists).isTrue();
    }
}


