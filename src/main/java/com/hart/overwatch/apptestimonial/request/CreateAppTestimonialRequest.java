package com.hart.overwatch.apptestimonial.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAppTestimonialRequest {

    @NotNull
    private Long userId;

    @Size(min = 1, max = 50, message = "Developer type must be between 1 and 50 characters")
    private String developerType;

    @Size(min = 1, max = 200, message = "Testimonial must be between 1 and 200 characters")
    private String content;

    public CreateAppTestimonialRequest() {

    }

    public CreateAppTestimonialRequest(Long userId, String developerType, String content) {
        this.userId = userId;
        this.developerType = developerType;
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getDeveloperType() {
        return developerType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeveloperType(String developerType) {
        this.developerType = developerType;
    }
}
