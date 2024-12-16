package com.hart.overwatch.suggestion.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.suggestion.FeedbackStatus;
import com.hart.overwatch.suggestion.FeedbackType;
import com.hart.overwatch.suggestion.PriorityLevel;

public class SuggestionDto {

    private Long id;

    private LocalDateTime createdAt;

    private String title;

    private String description;

    private String contact;

    private String fileUrl;

    private FeedbackType feedbackType;

    private PriorityLevel priorityLevel;

    private FeedbackStatus feedbackStatus;

    private String fullName;

    private String avatarUrl;


    public SuggestionDto() {

    }

    public SuggestionDto(Long id, LocalDateTime createdAt, String title, String description,
            String contact, String fileUrl, FeedbackType feedbackType, PriorityLevel priorityLevel,
            FeedbackStatus feedbackStatus, String fullName, String avatarUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.fileUrl = fileUrl;
        this.feedbackType = feedbackType;
        this.priorityLevel = priorityLevel;
        this.feedbackStatus = feedbackStatus;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContact() {
        return contact;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }
}
