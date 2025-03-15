package com.hart.overwatch.teampinnedmessage;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;
import com.hart.overwatch.teampinnedmessage.request.CreateTeamPinnedMessageRequest;
import com.hart.overwatch.teampinnedmessage.request.UpdateTeamPinnedMessageRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class TeamPinnedMessageService {

    private final long MAX_TEAM_PINNED_MESSAGES = 5L;

    private final TeamPinnedMessageRepository teamPinnedMessageRepository;

    private final UserService userService;

    private final TeamService teamService;

    @Autowired
    public TeamPinnedMessageService(TeamPinnedMessageRepository teamPinnedMessageRepository,
            UserService userService, TeamService teamService) {
        this.teamPinnedMessageRepository = teamPinnedMessageRepository;
        this.userService = userService;
        this.teamService = teamService;
    }

    public TeamPinnedMessage getTeamPinnedMessageById(Long teamPinnedMessageId) {
        return teamPinnedMessageRepository.findById(teamPinnedMessageId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Could not find team pinned message with the id %d", teamPinnedMessageId)));
    }

    private boolean canCreateTeamPinnedMessage(long totalTeamPinnedMessages) {
        return !(totalTeamPinnedMessages >= MAX_TEAM_PINNED_MESSAGES);
    }

    public void createTeamPinnedMessage(Long teamId, CreateTeamPinnedMessageRequest request) {
        long totalTeamPinnedMessages = teamPinnedMessageRepository.totalTeamPinnedMessages(teamId);
        if (!canCreateTeamPinnedMessage(totalTeamPinnedMessages)) {
            throw new BadRequestException(
                    String.format("You have exceeded the maximum amount of admin messages %d",
                            MAX_TEAM_PINNED_MESSAGES));
        }

        String cleanedMessage = Jsoup.clean(request.getMessage(), Safelist.none());
        Team team = teamService.getTeamByTeamId(teamId);

        if (!team.getUser().getId().equals(request.getUserId())) {
            throw new ForbiddenException("You do not have permission to post an admin message");
        }

        User user = userService.getUserById(request.getUserId());
        TeamPinnedMessage teamPinnedMessage = new TeamPinnedMessage();
        teamPinnedMessage.setTeam(team);
        teamPinnedMessage.setUser(user);
        teamPinnedMessage.setMessage(cleanedMessage);
        teamPinnedMessage.setIsEdited(false);
        teamPinnedMessage
                .setIndex(totalTeamPinnedMessages - 1 == -1 ? 0 : (int) totalTeamPinnedMessages);

        teamPinnedMessageRepository.save(teamPinnedMessage);
    }

    private List<TeamPinnedMessageDto> sortMessagesByIndex(List<TeamPinnedMessageDto> messages) {
        return messages.stream().filter(v -> v.getIndex() != null)
                .sorted(Comparator.comparingInt(TeamPinnedMessageDto::getIndex))
                .collect(Collectors.toList());
    }

    public List<TeamPinnedMessageDto> getTeamPinnedMessages(Long teamId) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<TeamPinnedMessageDto> pageResult =
                teamPinnedMessageRepository.getTeamPinnedMessagesByTeamId(teamId, pageable);
        return sortMessagesByIndex(pageResult.getContent());
    }

    public void updateTeamPinnedMessage(Long teamId, Long teamPinnedMessageId,
            UpdateTeamPinnedMessageRequest request) {
        String cleanedMessage = Jsoup.clean(request.getMessage(), Safelist.none());
        Team team = teamService.getTeamByTeamId(teamId);

        if (!team.getUser().getId().equals(request.getUserId())) {
            throw new ForbiddenException("You do not have permission to post an admin message");
        }

        TeamPinnedMessage teamPinnedMessage = getTeamPinnedMessageById(teamPinnedMessageId);

        teamPinnedMessage.setMessage(cleanedMessage);
        teamPinnedMessage.setIsEdited(true);

        teamPinnedMessageRepository.save(teamPinnedMessage);
    }

    public void deleteTeamPinnedMessage(Long teamId, Long teamPinnedMessageId) {
        Team team = teamService.getTeamByTeamId(teamId);
        User user = userService.getCurrentlyLoggedInUser();

        if (!team.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You do not have permission to post an admin message");
        }
        teamPinnedMessageRepository.deleteById(teamPinnedMessageId);
    }

    public List<TeamPinnedMessageDto> reorderTeamPinnedMessages(List<TeamPinnedMessageDto> dtos,
            Long teamId) {
        List<TeamPinnedMessage> entities = new ArrayList<>();

        for (int i = 0; i < dtos.size(); i++) {
            TeamPinnedMessage entity = getTeamPinnedMessageById(dtos.get(i).getId());
            entity.setIndex(i);
            entities.add(entity);
        }

        teamPinnedMessageRepository.saveAll(entities);

        return getTeamPinnedMessages(teamId);
    }
}
