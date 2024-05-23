package com.hart.overwatch.location.dto;


public class LocationDto {

    private String address;

    private String addressTwo;

    private String city;

    private String country;

    private String phoneNumber;

    private String state;

    private String zipCode;


    public LocationDto() {

    }

    public LocationDto(String address, String addressTwo, String city, String country,
            String phoneNumber, String state, String zipCode) {
        this.address = address;
        this.addressTwo = addressTwo;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

