package com.hart.overwatch.apptestimonial.dto;

public class MinAppTestimonialDto {

    private Long id;

    private String developerType;

    private String content;

    public MinAppTestimonialDto() {

    }

    public MinAppTestimonialDto(Long id, String developerType, String content) {
        this.id = id;
        this.developerType = developerType;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDeveloperType() {
        return developerType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeveloperType(String developerType) {
        this.developerType = developerType;
    }
}
