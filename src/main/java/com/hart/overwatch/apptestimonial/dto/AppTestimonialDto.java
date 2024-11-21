package com.hart.overwatch.apptestimonial.dto;

public class AppTestimonialDto {

    private Long id;

    private String firstName;

    private String developerType;

    private String content;

    private String avatarUrl;

    public AppTestimonialDto() {

    }

    public AppTestimonialDto(Long id, String firstName, String developerType, String content,
            String avatarUrl) {
        this.id = id;
        this.firstName = firstName;
        this.developerType = developerType;
        this.content = content;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDeveloperType(String developerType) {
        this.developerType = developerType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
