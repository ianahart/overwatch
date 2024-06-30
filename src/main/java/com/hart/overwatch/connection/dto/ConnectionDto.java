package com.hart.overwatch.connection.dto;

public class ConnectionDto {

    private Long id;

    private Long receiverId;

    private Long senderId;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    private String email;

    private String city;

    private String country;

    private String phoneNumber;

    private String bio;

    private String lastMessage;

    public ConnectionDto(Long id, Long receiverId, Long senderId, String firstName, String lastName,
            String avatarUrl, String email, String city, String country, String phoneNumber,
            String bio) {

        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getBio() {
        return bio;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getCountry() {
        return country;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
