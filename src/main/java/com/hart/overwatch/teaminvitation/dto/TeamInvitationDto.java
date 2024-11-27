package com.hart.overwatch.teaminvitation.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.teaminvitation.InvitationStatus;

public class TeamInvitationDto {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private Long teamId;

    private InvitationStatus status;

    private String senderAvatarUrl;

    private String senderFullName;

    private String teamName;

    private LocalDateTime createdAt;

    public TeamInvitationDto() {

    }

    public TeamInvitationDto(Long id, Long senderId, Long receiverId, Long teamId,
            InvitationStatus status, String senderAvatarUrl, String senderFullName, String teamName,
            LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.teamId = teamId;
        this.status = status;
        this.senderAvatarUrl = senderAvatarUrl;
        this.senderFullName = senderFullName;
        this.teamName = teamName;
        this.createdAt = createdAt;

    }

    public Long getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
