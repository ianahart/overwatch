package com.hart.overwatch.testimonial.response;

public class DeleteTestimonialResponse {

    private String message;


    public DeleteTestimonialResponse() {

    }

    public DeleteTestimonialResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
