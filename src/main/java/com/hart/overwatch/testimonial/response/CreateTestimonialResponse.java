package com.hart.overwatch.testimonial.response;

public class CreateTestimonialResponse {

    private String message;

    public CreateTestimonialResponse() {

    }

    public CreateTestimonialResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
