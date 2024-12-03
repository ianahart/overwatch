package com.hart.overwatch.teammember;

import java.util.List;
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
import com.hart.overwatch.teammember.dto.TeamMemberDto;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_member_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamMemberRepositoryTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

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

    private TeamMember teamMember;

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

    private TeamMember createTeamMember(User user, Team team) {
        TeamMember teamMemberEntity = new TeamMember();
        teamMemberEntity.setUser(user);
        teamMemberEntity.setTeam(team);
        teamMemberRepository.save(teamMemberEntity);

        return teamMemberEntity;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        teamMember = createTeamMember(user, team);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamMemberRepository.deleteAll();
        teamRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TeamMemberRepository_CountTeamMemberTeams_ReturnLongCount() {
        Long userId = user.getId();

        long count = teamMemberRepository.countTeamMemberTeams(userId);

        Assertions.assertThat(count).isEqualTo(1L);
    }

    @Test
    public void TeamMemberRepository_ExistsByTeamIdAndUserId_ReturnBooleanTrue() {
        Long teamId = team.getId();
        Long userId = user.getId();

        boolean exists = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void TeamMemberRepository_GetTeamsByTeamMember_ReturnPageOfTeamMemberTeamDto() {
        int page = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);
        Long userId = user.getId();

        Page<TeamMemberTeamDto> result =
                teamMemberRepository.getTeamsByTeamMember(pageable, userId);

        Assertions.assertThat(result).isNotEmpty();
        List<TeamMemberTeamDto> teamMemberTeamDtos = result.getContent();
        Assertions.assertThat(teamMemberTeamDtos).hasSize(1);
        TeamMemberTeamDto teamMemberTeamDto = teamMemberTeamDtos.get(0);
        Assertions.assertThat(teamMemberTeamDto.getId()).isEqualTo(teamMember.getId());
        Assertions.assertThat(teamMemberTeamDto.getTeamId())
                .isEqualTo(teamMember.getTeam().getId());
        Assertions.assertThat(teamMemberTeamDto.getUserId())
                .isEqualTo(teamMember.getUser().getId());
        Assertions.assertThat(teamMemberTeamDto.getTeamName())
                .isEqualTo(teamMember.getTeam().getTeamName());
    }

    @Test
    public void TeamMemberRepository_GetTeamMembersByTeamId_ReturnPageOfTeamMemberDto() {
        int page = 0;
        int pageSize = 3;
        Long teamId = team.getId();
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<TeamMemberDto> result = teamMemberRepository.getTeamMembersByTeamId(pageable, teamId);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        TeamMemberDto teamMemberDto = result.getContent().get(0);
        Assertions.assertThat(teamMemberDto).isNotNull();
        Assertions.assertThat(teamMemberDto.getId()).isEqualTo(teamMember.getId());
        Assertions.assertThat(teamMemberDto.getTeamId()).isEqualTo(teamMember.getTeam().getId());
        Assertions.assertThat(teamMemberDto.getUserId()).isEqualTo(teamMember.getUser().getId());
        Assertions.assertThat(teamMemberDto.getProfileId())
                .isEqualTo(teamMember.getUser().getProfile().getId());
        Assertions.assertThat(teamMemberDto.getFullName())
                .isEqualTo(teamMember.getUser().getFullName());
        Assertions.assertThat(teamMemberDto.getAvatarUrl())
                .isEqualTo(teamMember.getUser().getProfile().getAvatarUrl());
    }

    @Test
    public void TeamMemberRepository_SearchTeamMembersByTeamId_ReturnPageOfTeamMemberDto() {
        int page = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);
        Long teamId = team.getId();
        String search = "john doe";

        Page<TeamMemberDto> result = teamMemberRepository.searchTeamMembersByTeamId(pageable, teamId, search);
                Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        TeamMemberDto teamMemberDto = result.getContent().get(0);
        Assertions.assertThat(teamMemberDto).isNotNull();
        Assertions.assertThat(teamMemberDto.getId()).isEqualTo(teamMember.getId());
        Assertions.assertThat(teamMemberDto.getTeamId()).isEqualTo(teamMember.getTeam().getId());
        Assertions.assertThat(teamMemberDto.getUserId()).isEqualTo(teamMember.getUser().getId());
        Assertions.assertThat(teamMemberDto.getProfileId())
                .isEqualTo(teamMember.getUser().getProfile().getId());
        Assertions.assertThat(teamMemberDto.getFullName())
                .isEqualTo(teamMember.getUser().getFullName());
        Assertions.assertThat(teamMemberDto.getAvatarUrl())
                .isEqualTo(teamMember.getUser().getProfile().getAvatarUrl());

    } 
}


