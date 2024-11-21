package com.hart.overwatch.apptestimonial.response;

public class CreateAppTestimonialResponse {

    private String message;

    public CreateAppTestimonialResponse() {

    }

    public CreateAppTestimonialResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
