package com.hart.overwatch.teammessage;

import org.springframework.data.redis.connection.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teammember.TeamMemberService;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;

@Service
public class TeamMessageSubscriber implements MessageListener {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamMessageService teamMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;



    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {

        TeamMessageDto teamMessage = teamMessageService.createTeamMessage(message.toString());
        if (teamMessage == null) {
            return;
        }

        Team team = teamService.getTeamByTeamId(teamMessage.getTeamId());
        Long adminId = team.getUser().getId();
        List<Long> usersToSendTo = teamMemberService.getTeamMemberIds(team);
        usersToSendTo.add(adminId);

        for (Long userId : usersToSendTo) {
            this.messagingTemplate.convertAndSendToUser(String.valueOf(userId),
                    "/topic/team-messages", teamMessage);
        }

    }
}

