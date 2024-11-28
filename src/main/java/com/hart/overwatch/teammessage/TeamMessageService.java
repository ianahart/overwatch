package com.hart.overwatch.teammessage;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import java.util.List;

@Service
public class TeamMessageService {

    private final TeamMessageRepository teamMessageRepository;

    private final TeamService teamService;

    private final UserService userService;

    @Autowired
    public TeamMessageService(TeamMessageRepository teamMessageRepository, TeamService teamService,
            UserService userService) {
        this.teamMessageRepository = teamMessageRepository;
        this.teamService = teamService;
        this.userService = userService;
    }



    public List<TeamMessageDto> getTeamMessages(Long teamId) {
        return teamMessageRepository.getTeamMessagesByTeamId(teamId);
    }

    private TeamMessageDto convertToDto(TeamMessage teamMessage) {
        TeamMessageDto teamMessageDto = new TeamMessageDto();
        teamMessageDto.setId(teamMessage.getId());
        teamMessageDto.setText(teamMessage.getText());
        teamMessageDto.setCreatedAt(teamMessage.getCreatedAt());
        teamMessageDto.setUserId(teamMessage.getUser().getId());
        teamMessageDto.setFullName(teamMessage.getUser().getFullName());
        teamMessageDto.setAvatarUrl(teamMessage.getUser().getProfile().getAvatarUrl());
        teamMessageDto.setTeamId(teamMessage.getTeam().getId());

        return teamMessageDto;
    }

    public TeamMessageDto createTeamMessage(String messageJson) {

        JSONObject message = new JSONObject(messageJson);

        Long teamId = Long.valueOf(message.getLong("teamId"));
        Long userId = Long.valueOf(message.getLong("userId"));
        String text = Jsoup.clean(message.getString("text"), Safelist.none());

        User user = userService.getUserById(userId);
        Team team = teamService.getTeamByTeamId(teamId);

        TeamMessage teamMessage = new TeamMessage(text, user, team);

        teamMessageRepository.save(teamMessage);

        return convertToDto(teamMessage);
    }
}
