package com.hart.overwatch.customfield.request;

public class UpdateCustomFieldRequest {

    private Boolean isActive;

    public UpdateCustomFieldRequest() {

    }

    public UpdateCustomFieldRequest(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
