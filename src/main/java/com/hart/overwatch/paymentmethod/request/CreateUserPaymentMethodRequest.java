package com.hart.overwatch.paymentmethod.request;

public class CreateUserPaymentMethodRequest {

    private String id;

    private String city;

    private String country;

    private String line1;

    private String line2;

    private String postalCode;

    private String name;

    private String displayBrand;

    private String type;

    private Integer expMonth;

    private Integer expYear;

    public CreateUserPaymentMethodRequest() {

    }

    public CreateUserPaymentMethodRequest(String id, String city, String country, String line1,
            String line2, String postalCode, String name, String displayBrand, String type,
            Integer expMonth, Integer expYear) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.line1 = line1;
        this.line2 = line2;
        this.postalCode = postalCode;
        this.name = name;
        this.displayBrand = displayBrand;
        this.type = type;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getLine1() {
        return line1;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public String getLine2() {
        return line2;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getDisplayBrand() {
        return displayBrand;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setDisplayBrand(String displayBrand) {
        this.displayBrand = displayBrand;
    }
}
