package com.hart.overwatch.profile.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class AllProfileDto {

    private Long id;

    private Long userId;

    private String fullName;

    private String avatarUrl;

    private String country;

    private Timestamp createdAt;

    private List<FullAvailabilityDto> availability;

    private List<ItemDto> programmingLanguages;

    private FullPackageDto basic;

    private LocalDateTime lastActive;

    // Not in constructor


    private Boolean weekendsAvailable;

    private Long numOfReviews;

    private Float reviewAvgRating;

    private Boolean isFavorited;

    private String lastActiveReadable;

    public AllProfileDto() {

    }

    public AllProfileDto(Long id, Long userId, String fullName, String avatarUrl, String country,
            Timestamp createdAt, List<FullAvailabilityDto> availability,
            List<ItemDto> programmingLanguages, FullPackageDto basic, LocalDateTime lastActive) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.createdAt = createdAt;
        this.availability = availability;
        this.programmingLanguages = programmingLanguages;
        this.basic = basic;
        this.lastActive = lastActive;
    }

    public FullPackageDto getBasic() {
        return basic;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }


    public Long getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public Long getUserId() {
        return userId;
    }

    public String getCountry() {
        return country;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getNumOfReviews() {
        return numOfReviews;
    }

    public Boolean getIsFavorited() {
        return isFavorited;
    }

    public Float getReviewAvgRating() {
        return reviewAvgRating;
    }

    public Boolean getWeekendsAvailable() {
        return weekendsAvailable;
    }

    public List<FullAvailabilityDto> getAvailability() {
        return availability;
    }

    public List<ItemDto> getProgrammingLanguages() {
        return programmingLanguages;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setNumOfReviews(Long numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public void setReviewAvgRating(Float reviewAvgRating) {
        this.reviewAvgRating = reviewAvgRating;
    }

    public void setWeekendsAvailable(Boolean weekendsAvailable) {
        this.weekendsAvailable = weekendsAvailable;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLastActiveReadable() {
        return lastActiveReadable;
    }

    public void setAvailability(List<FullAvailabilityDto> availability) {
        this.availability = availability;
    }

    public void setProgrammingLanguages(List<ItemDto> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setBasic(FullPackageDto basic) {
        this.basic = basic;
    }

    public void setIsFavorited(Boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public void setLastActiveReadable(String lastActiveReadable) {
        this.lastActiveReadable = lastActiveReadable;
    }

}
