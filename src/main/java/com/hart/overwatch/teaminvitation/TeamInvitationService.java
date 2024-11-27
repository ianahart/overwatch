package com.hart.overwatch.teaminvitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;
import com.hart.overwatch.teaminvitation.request.CreateTeamInvitationRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;

@Service
public class TeamInvitationService {

    private final TeamInvitationRepository teamInvitationRepository;

    private final UserService userService;

    private final TeamService teamService;

    private final PaginationService paginationService;

    @Autowired
    public TeamInvitationService(TeamInvitationRepository teamInvitationRepository,
            UserService userService, TeamService teamService, PaginationService paginationService) {
        this.teamInvitationRepository = teamInvitationRepository;
        this.userService = userService;
        this.teamService = teamService;
        this.paginationService = paginationService;
    }

    private TeamInvitation getTeamInvitationByTeamInvitationId(Long teamInvitationId) {
        return teamInvitationRepository.findById(teamInvitationId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find team invitation with id %d", teamInvitationId)));
    }

    private boolean alreadySentTeamInvitation(Long senderId, Long receiverId, Long teamId) {
        return teamInvitationRepository.existsByReceiverIdAndSenderIdAndTeamId(receiverId, senderId,
                teamId);
    }

    public void createTeamInvitation(CreateTeamInvitationRequest request, Long teamId) {
        Long receiverId = request.getReceiverId();
        Long senderId = request.getSenderId();

        if (alreadySentTeamInvitation(senderId, receiverId, teamId)) {
            throw new BadRequestException(
                    "You have already sent this person an invitation to this team");
        }

        User receiver = userService.getUserById(receiverId);
        User sender = userService.getUserById(senderId);
        Team team = teamService.getTeamByTeamId(teamId);

        TeamInvitation teamInvitation = new TeamInvitation();

        teamInvitation.setTeam(team);
        teamInvitation.setSender(sender);
        teamInvitation.setReceiver(receiver);
        teamInvitation.setStatus(InvitationStatus.PENDING);

        teamInvitationRepository.save(teamInvitation);
    }

    public PaginationDto<TeamInvitationDto> getReceiverTeamInvitations(Long receiverId, int page,
            int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<TeamInvitationDto> result =
                teamInvitationRepository.getTeamInvitationsByReceiverId(pageable, receiverId);

        return new PaginationDto<TeamInvitationDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

    public void deleteTeamInvitation(Long teamInvitationId) {
        User user = userService.getCurrentlyLoggedInUser();
        TeamInvitation teamInvitation = getTeamInvitationByTeamInvitationId(teamInvitationId);

        if (!user.getId().equals(teamInvitation.getReceiver().getId())) {
            throw new ForbiddenException(
                    "You are forbidden from deleting another user's team invitation");
        }

        teamInvitationRepository.delete(teamInvitation);
    }

}
