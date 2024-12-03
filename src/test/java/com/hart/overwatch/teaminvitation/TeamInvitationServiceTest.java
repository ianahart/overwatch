package com.hart.overwatch.teaminvitation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;
import com.hart.overwatch.teaminvitation.request.CreateTeamInvitationRequest;
import com.hart.overwatch.teammember.TeamMemberService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamInvitationServiceTest {

    @InjectMocks
    private TeamInvitationService teamInvitationService;

    @Mock
    private TeamInvitationRepository teamInvitationRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    @Mock
    private TeamService teamService;

    @Mock
    private TeamMemberService teamMemberService;

    private User sender;

    private User receiver;

    private Team team;

    private TeamInvitation teamInvitation;


    private User createSender() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(1L);
        User senderEntity = new User("jane@mail.com", "Jane", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        senderEntity.setId(1L);

        return senderEntity;
    }


    private User createReceiver() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(2L);
        User receiverEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER,
                loggedIn, profileEntity, "Test12345%", new Setting());
        receiverEntity.setId(2L);

        return receiverEntity;
    }

    private Team createTeam(User sender) {
        Team teamEntity = new Team();
        teamEntity.setUser(sender);
        teamEntity.setTeamName("team one");
        teamEntity.setId(1L);

        return teamEntity;
    }

    private TeamInvitation createTeamInvitation(User sender, User receiver, Team team) {
        TeamInvitation teamInvitationEntity = new TeamInvitation();
        teamInvitationEntity.setTeam(team);
        teamInvitationEntity.setSender(sender);
        teamInvitationEntity.setReceiver(receiver);
        teamInvitationEntity.setStatus(InvitationStatus.PENDING);
        teamInvitationEntity.setId(1L);

        return teamInvitationEntity;
    }

    private TeamInvitationDto convertToDto(TeamInvitation teamInvitation) {
        TeamInvitationDto teamInvitationDto = new TeamInvitationDto();
        teamInvitationDto.setId(teamInvitation.getId());
        teamInvitationDto.setTeamId(teamInvitation.getTeam().getId());
        teamInvitationDto.setSenderId(teamInvitation.getSender().getId());
        teamInvitationDto.setTeamName(teamInvitation.getTeam().getTeamName());
        teamInvitationDto.setReceiverId(teamInvitation.getReceiver().getId());
        teamInvitationDto.setStatus(teamInvitation.getStatus());
        teamInvitationDto.setSenderFullName(teamInvitation.getSender().getFullName());
        teamInvitationDto
                .setSenderAvatarUrl(teamInvitation.getSender().getProfile().getAvatarUrl());

        return teamInvitationDto;
    }

    @BeforeEach
    public void setUp() {
        sender = createSender();
        receiver = createReceiver();
        team = createTeam(sender);
        teamInvitation = createTeamInvitation(sender, receiver, team);
    }

    @Test
    public void TeamInvitationService_CreateTeamInvitation_ThrowBadRequestException() {
        CreateTeamInvitationRequest request = new CreateTeamInvitationRequest();
        request.setSenderId(sender.getId());
        request.setReceiverId(receiver.getId());
        Long teamId = team.getId();

        when(teamInvitationRepository.existsByReceiverIdAndSenderIdAndTeamId(
                request.getReceiverId(), request.getSenderId(), teamId)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            teamInvitationService.createTeamInvitation(request, teamId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already sent this person an invitation to this team");
    }

    @Test
    public void TeamInvitationService_CreateTeamInvitation_ReturnNothing() {
        CreateTeamInvitationRequest request = new CreateTeamInvitationRequest();
        request.setSenderId(sender.getId());
        request.setReceiverId(receiver.getId());
        Long teamId = team.getId();

        when(teamInvitationRepository.existsByReceiverIdAndSenderIdAndTeamId(
                request.getReceiverId(), request.getSenderId(), teamId)).thenReturn(false);
        when(userService.getUserById(request.getSenderId())).thenReturn(sender);
        when(userService.getUserById(request.getReceiverId())).thenReturn(receiver);
        when(teamService.getTeamByTeamId(teamId)).thenReturn(team);
        when(teamInvitationRepository.save(any(TeamInvitation.class))).thenReturn(teamInvitation);

        teamInvitationService.createTeamInvitation(request, teamId);

        Assertions.assertThatNoException();
        verify(teamInvitationRepository, times(1)).save(any(TeamInvitation.class));
    }

    @Test
    public void TeamInvitationService_GetReceiverTeamInvitations_ReturnPaginationDtoOfTeamInvitationDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TeamInvitationDto teamInvitationDto = convertToDto(teamInvitation);
        Page<TeamInvitationDto> pageResult =
                new PageImpl<>(Collections.singletonList(teamInvitationDto), pageable, 1);
        PaginationDto<TeamInvitationDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(teamInvitationRepository.getTeamInvitationsByReceiverId(pageable, receiver.getId()))
                .thenReturn(pageResult);

        PaginationDto<TeamInvitationDto> actualPaginationDto = teamInvitationService
                .getReceiverTeamInvitations(receiver.getId(), page, pageSize, direction);

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
        TeamInvitationDto actualTeamInvitationDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualTeamInvitationDto.getId()).isEqualTo(teamInvitationDto.getId());
        Assertions.assertThat(actualTeamInvitationDto.getTeamId())
                .isEqualTo(teamInvitationDto.getTeamId());
        Assertions.assertThat(actualTeamInvitationDto.getSenderId())
                .isEqualTo(teamInvitationDto.getSenderId());
        Assertions.assertThat(actualTeamInvitationDto.getReceiverId())
                .isEqualTo(teamInvitationDto.getReceiverId());
        Assertions.assertThat(actualTeamInvitationDto.getTeamName())
                .isEqualTo(teamInvitationDto.getTeamName());
        Assertions.assertThat(actualTeamInvitationDto.getSenderFullName())
                .isEqualTo(teamInvitationDto.getSenderFullName());
        Assertions.assertThat(actualTeamInvitationDto.getSenderAvatarUrl())
                .isEqualTo(teamInvitationDto.getSenderAvatarUrl());
        Assertions.assertThat(actualTeamInvitationDto.getStatus())
                .isEqualTo(teamInvitationDto.getStatus());

    }

}


