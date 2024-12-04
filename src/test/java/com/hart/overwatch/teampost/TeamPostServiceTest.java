package com.hart.overwatch.teampost;

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
import com.hart.overwatch.teampost.dto.TeamPostDto;
import com.hart.overwatch.teampost.request.CreateTeamPostRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private TeamPostDto convertToDto(TeamPost teamPost) {
        TeamPostDto teamPostDto = new TeamPostDto();
        teamPostDto.setId(teamPost.getId());
        teamPostDto.setCode(teamPost.getCode());
        teamPostDto.setTeamId(teamPost.getTeam().getId());
        teamPostDto.setUserId(teamPost.getUser().getId());
        teamPostDto.setFullName(teamPost.getUser().getFullName());
        teamPostDto.setLanguage(teamPost.getLanguage());
        teamPostDto.setIsEdited(teamPost.getIsEdited());
        teamPostDto.setAvatarUrl(teamPost.getUser().getProfile().getAvatarUrl());

        return teamPostDto;
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

    @Test
    public void TeamPostService_GetTeamPosts_ReturnPaginationDtoOfTeamPostDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamPostDto teamPostDto = convertToDto(teamPosts.get(0));
        Page<TeamPostDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamPostDto), pageable, 1);
        PaginationDto<TeamPostDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamPostRepository.getTeamPostsByTeamId(pageable, team.getId()))
                .thenReturn(pageResult);

        PaginationDto<TeamPostDto> actualPaginationDto =
                teamPostService.getTeamPosts(team.getId(), page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto).isNotNull();
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        TeamPostDto actualTeamPostDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamPostDto.getId()).isEqualTo(teamPostDto.getId());
        Assertions.assertThat(actualTeamPostDto.getTeamId()).isEqualTo(teamPostDto.getTeamId());
        Assertions.assertThat(actualTeamPostDto.getUserId()).isEqualTo(teamPostDto.getUserId());
        Assertions.assertThat(actualTeamPostDto.getIsEdited()).isEqualTo(teamPostDto.getIsEdited());
        Assertions.assertThat(actualTeamPostDto.getCode()).isEqualTo(teamPostDto.getCode());
        Assertions.assertThat(actualTeamPostDto.getFullName()).isEqualTo(teamPostDto.getFullName());
        Assertions.assertThat(actualTeamPostDto.getLanguage()).isEqualTo(teamPostDto.getLanguage());
        Assertions.assertThat(actualTeamPostDto.getAvatarUrl())
                .isEqualTo(teamPostDto.getAvatarUrl());
    }

    @Test
    public void TeamPostService_DeleteTeamPost_ThrowForbiddenException() {
        Long teamPostId = teamPosts.get(0).getId();
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(teamPostRepository.findById(teamPostId)).thenReturn(Optional.of(teamPosts.get(0)));

        Assertions.assertThatThrownBy(() -> {
            teamPostService.deleteTeamPost(teamPostId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have the permission to delete another user's post");
    }

    @Test
    public void TeamPostService_DeleteTeamPost_ReturnNothing() {
        TeamPost teamPost = teamPosts.get(0);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(teamPostRepository.findById(teamPost.getId())).thenReturn(Optional.of(teamPost));

        doNothing().when(teamPostRepository).delete(teamPost);

        teamPostService.deleteTeamPost(teamPost.getId());

        verify(userService, times(1)).getCurrentlyLoggedInUser();
        verify(teamPostRepository, times(1)).findById(teamPost.getId());
        verify(teamPostRepository, times(1)).delete(teamPost);
    }

}


