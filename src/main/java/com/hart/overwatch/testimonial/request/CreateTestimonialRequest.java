package com.hart.overwatch.testimonial.request;

import jakarta.validation.constraints.Size;

public class CreateTestimonialRequest {

    private Long userId;

    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Size(min = 1, max = 300, message = "Testimonial must be between 1 and 300 characters")
    private String text;


    public CreateTestimonialRequest() {

    }

    public CreateTestimonialRequest(Long userId, String name, String text) {
        this.userId = userId;
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
