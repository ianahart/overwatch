package com.hart.overwatch.apptestimonial.response;

public class UpdateAdminAppTestimonialResponse {

    private String message;

    private Boolean data;

    public UpdateAdminAppTestimonialResponse() {

    }

    public UpdateAdminAppTestimonialResponse(String message, Boolean data) {
        this.message = message;
        this.data = data;
    }

    public Boolean getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
