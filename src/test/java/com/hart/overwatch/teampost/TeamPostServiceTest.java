package com.hart.overwatch.teampost;

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
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teamcomment.TeamComment;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;
import com.hart.overwatch.teampost.request.CreateTeamPostRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamPostServiceTest {

    @InjectMocks
    private TeamPostService teamPostService;

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

    private List<TeamPost> teamPosts;

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


    private List<TeamPost> createTeamPosts(User user, Team team, int numOfPosts) {
        List<TeamPost> teamPosts = new ArrayList<>();
        for (int i = 0; i < numOfPosts; i++) {
            TeamPost teamPost = new TeamPost();
            teamPost.setId(Long.valueOf(i + 1));
            teamPost.setTeam(team);
            teamPost.setUser(user);
            teamPost.setLanguage("javascript");
            teamPost.setIsEdited(false);
            teamPost.setCode(getCode());

            teamPosts.add(teamPost);
        }

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
            teamComment.setId(Long.valueOf(i + 1));
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

    @Test
    public void TeamPostService_CreateTeamPost_ReturnNothing() {
        Long teamId = team.getId();
        CreateTeamPostRequest request = new CreateTeamPostRequest();
        request.setUserId(user.getId());
        request.setLanguage("javascript");
        String code = """
                  function fetchData(url) {
                      return data;
                  }
                """;
        request.setCode(code);

        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(teamService.getTeamByTeamId(teamId)).thenReturn(team);

        TeamPost newTeamPost = new TeamPost();
        newTeamPost.setCode("<pre><code>" + request.getCode());
        newTeamPost.setLanguage(request.getLanguage());
        newTeamPost.setIsEdited(false);
        newTeamPost.setUser(user);
        newTeamPost.setTeam(team);

        when(teamPostRepository.save(any(TeamPost.class))).thenReturn(newTeamPost);

        teamPostService.createTeamPost(request, teamId);

        verify(userService, times(1)).getUserById(request.getUserId());
        verify(teamService, times(1)).getTeamByTeamId(teamId);
        verify(teamPostRepository, times(1)).save(any(TeamPost.class));
    }

}


