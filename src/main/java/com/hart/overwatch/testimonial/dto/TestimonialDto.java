package com.hart.overwatch.testimonial.dto;

import java.sql.Timestamp;

public class TestimonialDto {
    private Long id;

    private Long userId;

    private String name;

    private String text;

    private Timestamp createdAt;


    public TestimonialDto() {

    }

    public TestimonialDto(Long id, Long userId, String name, String text, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
