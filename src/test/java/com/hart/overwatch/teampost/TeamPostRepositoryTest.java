package com.hart.overwatch.teampost;

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
import com.hart.overwatch.teampost.dto.TeamPostDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_team_post_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TeamPostRepositoryTest {

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

    private List<TeamPost> teamPosts;

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


    private List<TeamPost> createTeamPosts(User user, Team team, int numOfPosts) {
        List<TeamPost> teamPosts = new ArrayList<>();
        for (int i = 0; i < numOfPosts; i++) {
            TeamPost teamPost = new TeamPost();
            teamPost.setTeam(team);
            teamPost.setUser(user);
            teamPost.setLanguage("javascript");
            teamPost.setIsEdited(false);
            teamPost.setCode(getCode());

            teamPosts.add(teamPost);
        }

        teamPostRepository.saveAll(teamPosts);
        return teamPosts;
    }

    private void createTeamComments(User user, List<TeamPost> teamPosts) {
        for (int i = 0; i < teamPosts.size(); i++) {
            TeamComment teamComment = new TeamComment();
            teamComment.setTag(user.getFullName());
            teamComment.setContent("content");
            teamComment.setIsEdited(false);
            teamComment.setTeamPost(teamPosts.get(i));
            teamComment.setUser(user);
            teamCommentRepository.save(teamComment);
        }
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        int numOfPosts = 3;
        teamPosts = createTeamPosts(user, team, numOfPosts);
        createTeamComments(user, teamPosts);

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

    @Test
    public void TeamPostRepository_GetTeamPostsByTeamId_ReturnPageOfTeamPostDto() {
        int page = 0, pageSize = 3;
        Long teamId = team.getId();
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<TeamPostDto> result = teamPostRepository.getTeamPostsByTeamId(pageable, teamId);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(teamPosts.size());

        List<TeamPostDto> teamPostDtos = result.getContent();
        for (int i = 0; i < teamPostDtos.size(); i++) {
            Assertions.assertThat(teamPostDtos.get(i).getId()).isEqualTo(teamPosts.get(i).getId());
            Assertions.assertThat(teamPostDtos.get(i).getTeamId())
                    .isEqualTo(teamPosts.get(i).getTeam().getId());
            Assertions.assertThat(teamPostDtos.get(i).getUserId())
                    .isEqualTo(teamPosts.get(i).getUser().getId());
            Assertions.assertThat(teamPostDtos.get(i).getIsEdited())
                    .isEqualTo(teamPosts.get(i).getIsEdited());
            Assertions.assertThat(teamPostDtos.get(i).getHasComments()).isTrue();
            Assertions.assertThat(teamPostDtos.get(i).getCode())
                    .isEqualTo(teamPosts.get(i).getCode());
            Assertions.assertThat(teamPostDtos.get(i).getFullName())
                    .isEqualTo(teamPosts.get(i).getUser().getFullName());
            Assertions.assertThat(teamPostDtos.get(i).getLanguage())
                    .isEqualTo(teamPosts.get(i).getLanguage());
            Assertions.assertThat(teamPostDtos.get(i).getAvatarUrl())
                    .isEqualTo(teamPosts.get(i).getUser().getProfile().getAvatarUrl());
        }
    }

}


