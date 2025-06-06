package com.hart.overwatch.teampinnedmessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Arrays;
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
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.request.UpdateTeamPinnedMessageRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamPinnedMessageServiceTest {

    @InjectMocks
    private TeamPinnedMessageService teamPinnedMessageService;

    @Mock
    private TeamPinnedMessageRepository teamPinnedMessageRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private User user;

    private Team team;

    private List<TeamPinnedMessage> teamPinnedMessages = new ArrayList<>();

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
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

    private List<TeamPinnedMessage> createTeamPinnedMessages(User user, Team team, int count) {
        List<TeamPinnedMessage> messages = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            TeamPinnedMessage message = new TeamPinnedMessage();
            message.setId(Long.valueOf(i + 1));
            message.setTeam(team);
            message.setUser(user);
            message.setIndex(i);
            message.setMessage(String.format("message-%d", i));
            message.setIsEdited(false);
            messages.add(message);
        }

        return messages;
    }


    private TeamPinnedMessageDto convertToDto(TeamPinnedMessage message) {
        TeamPinnedMessageDto dto = new TeamPinnedMessageDto();
        dto.setId(message.getId());
        dto.setUserId(message.getUser().getId());
        dto.setIndex(message.getIndex());
        dto.setMessage(message.getMessage());
        dto.setFullName(message.getUser().getFullName());
        dto.setIsEdited(message.getIsEdited());
        dto.setAvatarUrl(message.getUser().getProfile().getAvatarUrl());

        return dto;
    }


    @BeforeEach
    public void setUp() {
        int numOfMessages = 3;
        user = createUser();
        team = createTeam(user);
        teamPinnedMessages = createTeamPinnedMessages(user, team, numOfMessages);

    }

    @Test
    public void TeamPinnedMessageService_GetTeamPinnedMessageById_ThrowsNotFoundException() {
        Long teamPinnedMessageId = 999L;

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.getTeamPinnedMessageById(teamPinnedMessageId);
        }).isInstanceOf(NotFoundException.class).hasMessage(String
                .format("Could not find team pinned message with the id %d", teamPinnedMessageId));

    }

    @Test
    public void TeamPinnedMessageService_GetTeamPinnedMessageById_ReturnTeamPinnedMessage() {
        TeamPinnedMessage teamPinnedMessage = teamPinnedMessages.get(0);

        when(teamPinnedMessageRepository.findById(teamPinnedMessage.getId()))
                .thenReturn(Optional.of(teamPinnedMessage));

        TeamPinnedMessage returnedTeamPinnedMessage =
                teamPinnedMessageService.getTeamPinnedMessageById(teamPinnedMessage.getId());

        Assertions.assertThat(returnedTeamPinnedMessage).isNotNull();
        Assertions.assertThat(returnedTeamPinnedMessage.getId())
                .isEqualTo(teamPinnedMessage.getId());
    }

    @Test
    public void teamPinnedMessageService_CreateTeamPinnedMessage_Throw_BadRequestException() {
        CreateTeamPinnedMessageRequest request = new CreateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message 4");
        long MAX_TEAM_PINNED_MESSAGES = 5L;
        when(teamPinnedMessageRepository.totalTeamPinnedMessages(team.getId()))
                .thenReturn(MAX_TEAM_PINNED_MESSAGES + 1);

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.createTeamPinnedMessage(team.getId(), null);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                String.format("You have exceeded the maximum amount of admin messages %d",
                        MAX_TEAM_PINNED_MESSAGES));
    }

    @Test
    public void TeamPinnedMessageService_CreateTeamPinnedMessage_ThrowsForbiddenException() {
        CreateTeamPinnedMessageRequest request = new CreateTeamPinnedMessageRequest();
        long notAdminUserId = 999L;
        request.setUserId(notAdminUserId);
        request.setMessage("message 4");

        when(teamPinnedMessageRepository.totalTeamPinnedMessages(team.getId())).thenReturn(0L);
        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.createTeamPinnedMessage(team.getId(), request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have permission to post an admin message");
    }


    @Test
    public void TeamPinnedMessageService_CreateTeamPinnedMessage_Return_Nothing() {
        CreateTeamPinnedMessageRequest request = new CreateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message 4");
        long MAX_TEAM_PINNED_MESSAGES = 0L;
        when(teamPinnedMessageRepository.totalTeamPinnedMessages(team.getId()))
                .thenReturn(MAX_TEAM_PINNED_MESSAGES);
        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);
        when(userService.getUserById(user.getId())).thenReturn(user);
        TeamPinnedMessage teamPinnedMessage = new TeamPinnedMessage();
        teamPinnedMessage.setId(4L);
        teamPinnedMessage.setTeam(team);
        teamPinnedMessage.setUser(user);
        teamPinnedMessage.setMessage("message 4");
        teamPinnedMessage.setIsEdited(false);
        teamPinnedMessage.setIndex(3);

        when(teamPinnedMessageRepository.save(any(TeamPinnedMessage.class)))
                .thenReturn(teamPinnedMessage);

        teamPinnedMessageService.createTeamPinnedMessage(team.getId(), request);

        verify(teamPinnedMessageRepository, times(1)).save(any(TeamPinnedMessage.class));

    }

    @Test
    public void TeamPinnedMessageService_GetTeamPinnedMessages_Return_List_Of_TeamPinnedMessageDto() {
        Long teamId = team.getId();
        int pageSize = 5;
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamPinnedMessageDto teamPinnedMessageDto = convertToDto(teamPinnedMessages.get(0));
        Page<TeamPinnedMessageDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamPinnedMessageDto), pageable, 1);

        when(teamPinnedMessageRepository.getTeamPinnedMessagesByTeamId(teamId, pageable))
                .thenReturn(pageResult);

        List<TeamPinnedMessageDto> result = teamPinnedMessageService.getTeamPinnedMessages(teamId);
        TeamPinnedMessage expectedTeamPinnedMessage = teamPinnedMessages.get(0);
        TeamPinnedMessageDto actualTeamPinnedMessageDto = result.get(0);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(actualTeamPinnedMessageDto.getId())
                .isEqualTo(expectedTeamPinnedMessage.getId());
        Assertions.assertThat(actualTeamPinnedMessageDto.getUserId())
                .isEqualTo(expectedTeamPinnedMessage.getUser().getId());
        Assertions.assertThat(actualTeamPinnedMessageDto.getIndex())
                .isEqualTo(expectedTeamPinnedMessage.getIndex());
        Assertions.assertThat(actualTeamPinnedMessageDto.getIsEdited())
                .isEqualTo(expectedTeamPinnedMessage.getIsEdited());
        Assertions.assertThat(actualTeamPinnedMessageDto.getMessage())
                .isEqualTo(expectedTeamPinnedMessage.getMessage());
        Assertions.assertThat(actualTeamPinnedMessageDto.getFullName())
                .isEqualTo(expectedTeamPinnedMessage.getUser().getFullName());
        Assertions.assertThat(actualTeamPinnedMessageDto.getAvatarUrl())
                .isEqualTo(expectedTeamPinnedMessage.getUser().getProfile().getAvatarUrl());
    }

    @Test
    public void TeamPinnedMessageService_UpdateTeamPinnedMessage_ThrowForbiddenException() {
        UpdateTeamPinnedMessageRequest request = new UpdateTeamPinnedMessageRequest();
        request.setUserId(999L);
        request.setMessage("message edited");

        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.updateTeamPinnedMessage(team.getId(),
                    teamPinnedMessages.get(0).getId(), request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have permission to post an admin message");
    }

    @Test
    public void TeamPinnedMessageService_UpdateTeamPinnedMessage_ReturnNothing() {
        Long teamPinnedMessageId = teamPinnedMessages.get(0).getId();
        UpdateTeamPinnedMessageRequest request = new UpdateTeamPinnedMessageRequest();
        request.setUserId(user.getId());
        request.setMessage("message edited");

        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);

        when(teamPinnedMessageRepository.findById(teamPinnedMessageId))
                .thenReturn(Optional.of(teamPinnedMessages.get(0)));

        teamPinnedMessages.get(0).setMessage(request.getMessage());
        teamPinnedMessages.get(0).setIsEdited(true);

        when(teamPinnedMessageRepository.save(any(TeamPinnedMessage.class)))
                .thenReturn(teamPinnedMessages.get(0));

        teamPinnedMessageService.updateTeamPinnedMessage(teamPinnedMessageId, teamPinnedMessageId,
                request);

        verify(teamPinnedMessageRepository, times(1)).save(any(TeamPinnedMessage.class));
    }

    @Test
    public void TeamPinnedMessageService_DeleteTeamPinnedMessage_ThrowForbiddenException() {
        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            teamPinnedMessageService.deleteTeamPinnedMessage(team.getId(), teamPinnedMessages.get(0).getId());
        }).isInstanceOf(ForbiddenException.class).hasMessage("You do not have permission to post an admin message");
    }

    @Test
    public void TeamPinnedMessageService_DeleteTeamPinnedMessage_ReturnNothing() {
        long teamPinnedMessageId = teamPinnedMessages.get(0).getId();
        when(teamService.getTeamByTeamId(team.getId())).thenReturn(team);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        doNothing().when(teamPinnedMessageRepository).deleteById(teamPinnedMessageId);

        teamPinnedMessageService.deleteTeamPinnedMessage(team.getId(), teamPinnedMessageId);
        verify(teamPinnedMessageRepository, times(1)).deleteById(teamPinnedMessageId);
    }

    @Test
    public void TeamPinnedMessageService_ReorderTeamPinnedMessages_Return_List_Of_TeamPinnedMessageDto() {
        List<TeamPinnedMessageDto> inputDtos = Arrays.asList(
                convertToDto(teamPinnedMessages.get(1)), convertToDto(teamPinnedMessages.get(2)),
                convertToDto(teamPinnedMessages.get(0)));

        TeamPinnedMessageDto expectedDto1 = new TeamPinnedMessageDto();
        expectedDto1.setId(1L);
        expectedDto1.setIndex(2);
        expectedDto1.setMessage("Message 1");

        TeamPinnedMessageDto expectedDto2 = new TeamPinnedMessageDto();
        expectedDto2.setId(2L);
        expectedDto2.setIndex(0);
        expectedDto2.setMessage("Message 2");

        TeamPinnedMessageDto expectedDto3 = new TeamPinnedMessageDto();
        expectedDto3.setId(3L);
        expectedDto3.setIndex(1);
        expectedDto3.setMessage("Message 3");

        List<TeamPinnedMessageDto> expectedDtos =
                Arrays.asList(expectedDto2, expectedDto3, expectedDto1);

        when(teamPinnedMessageRepository.findById(1L))
                .thenReturn(Optional.of(teamPinnedMessages.get(1)));
        when(teamPinnedMessageRepository.findById(2L))
                .thenReturn(Optional.of(teamPinnedMessages.get(0)));
        when(teamPinnedMessageRepository.findById(3L))
                .thenReturn(Optional.of(teamPinnedMessages.get(2)));

        Page<TeamPinnedMessageDto> page = new PageImpl<>(expectedDtos);
        when(teamPinnedMessageRepository.getTeamPinnedMessagesByTeamId(eq(team.getId()),
                any(Pageable.class))).thenReturn(page);

        List<TeamPinnedMessageDto> result =
                teamPinnedMessageService.reorderTeamPinnedMessages(inputDtos, team.getId());

        verify(teamPinnedMessageRepository).saveAll(any());

        verify(teamPinnedMessageRepository).getTeamPinnedMessagesByTeamId(eq(team.getId()),
                any(Pageable.class));

        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result).extracting(TeamPinnedMessageDto::getId).containsExactly(2L,
                3L, 1L);
        Assertions.assertThat(result).extracting(TeamPinnedMessageDto::getIndex).containsExactly(0,
                1, 2);
    }

}


