package com.hart.overwatch.apptestimonial.request;

public class UpdateAdminAppTestimonialRequest {

    private Boolean isSelected;

    public UpdateAdminAppTestimonialRequest() {

    }

    public UpdateAdminAppTestimonialRequest(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

}
