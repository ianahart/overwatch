package com.hart.overwatch.apptestimonial.dto;

import java.time.LocalDateTime;

public class AdminAppTestimonialDto {

    private Long id;

    private String firstName;

    private String developerType;

    private String content;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private Boolean isSelected;

    public AdminAppTestimonialDto() {

    }

    public AdminAppTestimonialDto(Long id, String firstName, String developerType, String content,
            String avatarUrl, LocalDateTime createdAt, Boolean isSelected) {
        this.id = id;
        this.firstName = firstName;
        this.developerType = developerType;
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.isSelected = isSelected;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDeveloperType() {
        return developerType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDeveloperType(String developerType) {
        this.developerType = developerType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

