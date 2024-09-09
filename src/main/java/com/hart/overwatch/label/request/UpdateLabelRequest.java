package com.hart.overwatch.label.request;

public class UpdateLabelRequest {

    private Boolean isChecked;


    public UpdateLabelRequest() {

    }

    public UpdateLabelRequest(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
}
