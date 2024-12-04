package com.hart.overwatch.teamcomment;

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
import com.hart.overwatch.teamcomment.TeamComment;
import com.hart.overwatch.teamcomment.TeamCommentRepository;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.teampost.TeamPostRepository;
import com.hart.overwatch.teampost.dto.TeamPostDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_comment_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamCommentRepositoryTest {

    @Autowired
    private TeamPostRepository teamPostRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    TeamCommentRepository teamCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Team team;

    private TeamPost teamPost;

    private List<TeamComment> teamComments;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url");
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

    private String getCode() {
        return """
                <pre><code>function fetchData(url) {                                           +
                           return data;
                        }
                        """;
    }


    private TeamPost createTeamPost(User user, Team team) {
        TeamPost teamPostEntity = new TeamPost();
        teamPostEntity.setTeam(team);
        teamPostEntity.setUser(user);
        teamPostEntity.setLanguage("javascript");
        teamPostEntity.setIsEdited(false);
        teamPostEntity.setCode(getCode());

        teamPostRepository.save(teamPostEntity);

        return teamPostEntity;
    }

    private List<TeamComment> createTeamComments(User user, TeamPost teamPost, int numOfComments) {
        List<TeamComment> teamCommentEntities = new ArrayList<>();
        for (int i = 0; i < numOfComments; i++) {
            TeamComment teamCommentEntity = new TeamComment();
            teamCommentEntity.setTag(user.getFullName());
            teamCommentEntity.setUser(user);
            teamCommentEntity.setContent("content");
            teamCommentEntity.setIsEdited(false);
            teamCommentEntity.setTeamPost(teamPost);

            teamCommentEntities.add(teamCommentEntity);
        }
        teamCommentRepository.saveAll(teamCommentEntities);
        return teamCommentEntities;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        teamPost = createTeamPost(user, team);
        int numOfComments = 3;
        teamComments = createTeamComments(user, teamPost, numOfComments);


    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        teamCommentRepository.deleteAll();
        teamPostRepository.deleteAll();
        teamRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

}


