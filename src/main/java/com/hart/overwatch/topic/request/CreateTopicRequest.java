package com.hart.overwatch.topic.request;

import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateTopicRequest {

    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    @Size(max = 250, message = "Description must be under 250 characters")
    private String description;

    @Size(min = 1)
    private List<@Size(max = 30, message = "A tag must be under 30 characters") String> tags;

    private Long userId;


    public CreateTopicRequest() {

    }

    public CreateTopicRequest(String title, String description, List<String> tags) {
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
