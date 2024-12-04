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
import com.hart.overwatch.teamcomment.dto.MinTeamCommentDto;
import com.hart.overwatch.teamcomment.dto.TeamCommentDto;
import com.hart.overwatch.teamcomment.request.CreateTeamCommentRequest;
import com.hart.overwatch.teamcomment.request.UpdateTeamCommentRequest;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.teampost.TeamPostService;
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
    private TeamPostService teamPostService;

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

    private TeamCommentDto convertToDto(TeamComment teamComment) {
        TeamCommentDto teamCommentDto = new TeamCommentDto();
        teamCommentDto.setId(teamComment.getId());
        teamCommentDto.setTag(teamComment.getTag());
        teamCommentDto.setUserId(teamComment.getUser().getId());
        teamCommentDto.setContent(teamComment.getContent());
        teamCommentDto.setFullName(teamComment.getUser().getFullName());
        teamCommentDto.setIsEdited(teamComment.getIsEdited());
        teamCommentDto.setAvatarUrl(teamComment.getUser().getProfile().getAvatarUrl());
        teamCommentDto.setTeamPostId(teamComment.getTeamPost().getId());

        return teamCommentDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
        teamPost = createTeamPost(user, team);
        int numOfComments = 3;
        teamComments = createTeamComments(user, teamPost, numOfComments);
    }

    @Test
    public void TeamCommentService_GetTeamComments_ReturnPaginationDtoOfTeamCommentDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamCommentDto teamCommentDto = convertToDto(teamComments.get(0));
        Page<TeamCommentDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamCommentDto), pageable, 1);
        PaginationDto<TeamCommentDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamCommentRepository.getTeamCommentsByTeamPostId(pageable, teamPost.getId()))
                .thenReturn(pageResult);

        PaginationDto<TeamCommentDto> actualPaginationDto =
                teamCommentService.getTeamComments(teamPost.getId(), page, pageSize, direction);

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
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        TeamCommentDto actualTeamCommentDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamCommentDto.getId()).isEqualTo(teamCommentDto.getId());
        Assertions.assertThat(actualTeamCommentDto.getUserId())
                .isEqualTo(teamCommentDto.getUserId());
        Assertions.assertThat(actualTeamCommentDto.getTeamPostId())
                .isEqualTo(teamCommentDto.getTeamPostId());
        Assertions.assertThat(actualTeamCommentDto.getIsEdited())
                .isEqualTo(teamCommentDto.getIsEdited());
        Assertions.assertThat(actualTeamCommentDto.getTag()).isEqualTo(teamCommentDto.getTag());
        Assertions.assertThat(actualTeamCommentDto.getContent())
                .isEqualTo(teamCommentDto.getContent());
        Assertions.assertThat(actualTeamCommentDto.getFullName())
                .isEqualTo(teamCommentDto.getFullName());
        Assertions.assertThat(actualTeamCommentDto.getAvatarUrl())
                .isEqualTo(teamCommentDto.getAvatarUrl());
    }

    @Test
    public void TeamCommentService_CreateTeamComment_ReturnNothing() {
        Long teamPostId = teamPost.getId();
        CreateTeamCommentRequest request = new CreateTeamCommentRequest();
        request.setTag("John Doe");
        request.setUserId(user.getId());
        request.setContent("comment content");

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(teamPostService.getTeamPostByTeamPostId(teamPostId)).thenReturn(teamPost);

        TeamComment newTeamComment =
                new TeamComment(false, request.getContent(), user, teamPost, request.getTag());

        when(teamCommentRepository.save(any(TeamComment.class))).thenReturn(newTeamComment);

        teamCommentService.createTeamComment(request, teamPostId);

        verify(teamCommentRepository, times(1)).save(any(TeamComment.class));
    }

    @Test
    public void TeamCommentService_GetTeamComment_ReturnMinTeamCommentDto() {
        TeamComment teamComment = teamComments.get(0);
        when(teamCommentRepository.findById(teamComment.getId()))
                .thenReturn(Optional.of(teamComment));
        MinTeamCommentDto result = teamCommentService.getTeamComment(teamComment.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTag()).isEqualTo(teamComment.getTag());
        Assertions.assertThat(result.getContent()).isEqualTo(teamComment.getContent());
    }

    @Test
    public void TeamCommentService_UpdateTeamComment_ReturnMinTeamCommentDto() {
        TeamComment teamComment = teamComments.get(0);
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(teamCommentRepository.findById(teamComment.getId()))
                .thenReturn(Optional.of(teamComment));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        UpdateTeamCommentRequest request = new UpdateTeamCommentRequest();
        request.setTag(user.getFullName());
        request.setContent("updated comment");

        Assertions.assertThatThrownBy(() -> {
            teamCommentService.updateTeamComment(teamComment.getId(), request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have permission to update this comment");
    }

    @Test
    public void TeamCommentService_UpdateTeamComment_ReturnNothing() {
        TeamComment teamComment = teamComments.get(0);
        when(teamCommentRepository.findById(teamComment.getId()))
                .thenReturn(Optional.of(teamComment));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        UpdateTeamCommentRequest request = new UpdateTeamCommentRequest();
        request.setTag(user.getFullName());
        request.setContent("updated comment");

        when(teamCommentRepository.save(any(TeamComment.class))).thenReturn(teamComment);

        teamCommentService.updateTeamComment(teamComment.getId(), request);

        verify(teamCommentRepository, times(1)).save(any(TeamComment.class));
    }

    @Test
    public void TeamCommentService_DeleteTeamComment_ThrowForbiddenException() {
        TeamComment teamComment = teamComments.get(0);
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(teamCommentRepository.findById(teamComment.getId()))
                .thenReturn(Optional.of(teamComment));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            teamCommentService.deleteTeamComment(teamComment.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have permission to delete this comment");
    }

}


