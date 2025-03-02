package com.hart.overwatch.teampinnedmessage.request;

import java.util.List;
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;

public class ReorderTeamPinnedMessageRequest {

    private List<TeamPinnedMessageDto> teamPinnedMessages;

    public ReorderTeamPinnedMessageRequest() {

    }

    public ReorderTeamPinnedMessageRequest(List<TeamPinnedMessageDto> teamPinnedMessages) {
        this.teamPinnedMessages = teamPinnedMessages;
    }

    public List<TeamPinnedMessageDto> getTeamPinnedMessages() {
        return teamPinnedMessages;
    }

    public void setTeamPinnedMessages(List<TeamPinnedMessageDto> teamPinnedMessages) {
        this.teamPinnedMessages = teamPinnedMessages;
    }
}
