package com.hart.overwatch.apptestimonial.response;

public class DeleteAppTestimonialResponse {

    private String message;

    public DeleteAppTestimonialResponse() {

    }

    public DeleteAppTestimonialResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
