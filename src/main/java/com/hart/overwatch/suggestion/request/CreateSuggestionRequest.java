package com.hart.overwatch.suggestion.request;

import org.springframework.web.multipart.MultipartFile;
import com.hart.overwatch.suggestion.FeedbackType;
import com.hart.overwatch.suggestion.PriorityLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateSuggestionRequest {

    @NotNull(message = "userId is missing and cannot be empty")
    private Long userId;

    @Size(min = 1, max = 150, message = "Title must be between 1 and 150  characters")
    private String title;

    @Size(min = 1, max = 600, message = "Description must be between 1 and 600 characters")
    private String description;

    @Size(min = 1, max = 150, message = "Contact email must be between 1 and 150 characters")
    private String contact;

    private FeedbackType feedbackType;

    private PriorityLevel priorityLevel;

    private MultipartFile attachment;


    public CreateSuggestionRequest() {

    }

    public CreateSuggestionRequest(Long userId, String title, String description, String contact,
            FeedbackType feedbackType, PriorityLevel priorityLevel, MultipartFile attachment) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.feedbackType = feedbackType;
        this.priorityLevel = priorityLevel;
        this.attachment = attachment;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContact() {
        return contact;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public String getDescription() {
        return description;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}
