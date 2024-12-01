package com.hart.overwatch.team;

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
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.dto.TeamDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Team team;



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


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TeamRepository_GetTeamCountByUserId_ReturnLongCount() {

        Long teamCount = teamRepository.getTeamCountByUserId(user.getId());

        Assertions.assertThat(teamCount).isEqualTo(1L);
    }

    @Test
    public void TeamRepository_GetTeamsByUserId_ReturnPageOfTeamDto() {
        Long userId = user.getId();
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0, pageSize);

        Page<TeamDto> result = teamRepository.getTeamsByUserId(pageable, userId);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        TeamDto teamDto = result.getContent().get(0);
        Assertions.assertThat(teamDto.getId()).isEqualTo(team.getId());
        Assertions.assertThat(teamDto.getUserId()).isEqualTo(team.getUser().getId());
        Assertions.assertThat(teamDto.getTeamName()).isEqualTo(team.getTeamName());
        Assertions.assertThat(teamDto.getTeamDescription()).isEqualTo(team.getTeamDescription());
    }

}


