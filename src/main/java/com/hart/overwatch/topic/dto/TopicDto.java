package com.hart.overwatch.topic.dto;

import java.util.List;
import com.hart.overwatch.tag.dto.TagDto;


public class TopicDto {

    private Long id;

    private String title;

    private String description;

    private List<TagDto> tags;

    private Integer totalCommentCount;


    public TopicDto() {

    }

    public TopicDto(Long id, String title, String description, List<TagDto> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalCommentCount() {
        return totalCommentCount;
    }

    public String getTitle() {
        return title;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTotalCommentCount(Integer totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }
}
