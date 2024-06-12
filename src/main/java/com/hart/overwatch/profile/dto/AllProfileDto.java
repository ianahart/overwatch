package com.hart.overwatch.profile.dto;

import java.util.List;

public class AllProfileDto {

    private Long id;

    private Long userId;

    private String fullName;

    private String avatarUrl;

    private String country;

    private List<FullAvailabilityDto> availability;

    private List<ItemDto> programmingLanguages;

    public AllProfileDto() {

    }

    public AllProfileDto(Long id, Long userId, String fullName, String avatarUrl, String country,
            List<FullAvailabilityDto> availability, List<ItemDto> programmingLanguages) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.availability = availability;
        this.programmingLanguages = programmingLanguages;
    }

    public Long getId() {
        return id;
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

    public List<FullAvailabilityDto> getAvailability() {
        return availability;
    }

    public List<ItemDto> getProgrammingLanguages() {
        return programmingLanguages;
    }


    public void setId(Long id) {
        this.id = id;
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

    public void setAvailability(List<FullAvailabilityDto> availability) {
        this.availability = availability;
    }

    public void setProgrammingLanguages(List<ItemDto> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

}
