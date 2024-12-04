package com.hart.overwatch.teamcomment;

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
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teamcomment.TeamComment;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.teampost.TeamPostRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamCommentServiceTest {

    @InjectMocks
    private TeamCommentService teamCommentService;

    @Mock
    private TeamCommentRepository teamCommentRepository;

    @Mock
    private TeamPostRepository teamPostRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private User user;

    private Team team;

    private TeamPost teamPost;

    private List<TeamComment> teamComments;

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
        teamPostEntity.setId(1L);

        return teamPostEntity;
    }

    private List<TeamComment> createTeamComments(User user, TeamPost teamPost, int numOfComments) {
        List<TeamComment> teamCommentEntities = new ArrayList<>();
        for (int i = 0; i < numOfComments; i++) {
            TeamComment teamCommentEntity = new TeamComment();
            teamCommentEntity.setId(Long.valueOf(i + 1));
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

}


