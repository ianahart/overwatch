package com.hart.overwatch.profile.dto;

public class ProfileSetupDto {

    private String avatar;

    private String tagLine;

    private String bio;


    public ProfileSetupDto() {

    }

    public ProfileSetupDto(String avatar, String tagLine, String bio) {
        this.avatar = avatar;
        this.tagLine = tagLine;
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}
