package com.hart.overwatch.badge.request;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Size;

public class UpdateBadgeRequest {

    private MultipartFile image;

    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    public UpdateBadgeRequest() {

    }

    public UpdateBadgeRequest(MultipartFile image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            this.image = null;
        } else {
            this.image = image;
        }
    }
}

