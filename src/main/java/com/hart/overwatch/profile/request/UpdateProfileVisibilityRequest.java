package com.hart.overwatch.profile.request;

public class UpdateProfileVisibilityRequest {

    private Boolean isVisible;

    public UpdateProfileVisibilityRequest() {

    }

    public UpdateProfileVisibilityRequest(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
