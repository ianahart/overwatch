package com.hart.overwatch.teaminvitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teaminvitation.request.CreateTeamInvitationRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class TeamInvitationService {

    private final TeamInvitationRepository teamInvitationRepository;

    private final UserService userService;

    private final TeamService teamService;

    @Autowired
    public TeamInvitationService(TeamInvitationRepository teamInvitationRepository,
            UserService userService, TeamService teamService) {
        this.teamInvitationRepository = teamInvitationRepository;
        this.userService = userService;
        this.teamService = teamService;
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
}
