package com.hart.overwatch.favorite.request;

public class ToggleFavoriteRequest {

    private Long userId;

    private Long profileId;

    private Boolean isFavorited;


    public ToggleFavoriteRequest() {

    }

    public ToggleFavoriteRequest(Long userId, Long profileId, Boolean isFavorited) {
        this.userId = userId;
        this.profileId = profileId;
        this.isFavorited = isFavorited;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public Boolean getIsFavorited() {
        return isFavorited;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setIsFavorited(Boolean isFavorited) {
        this.isFavorited = isFavorited;
    }
}
