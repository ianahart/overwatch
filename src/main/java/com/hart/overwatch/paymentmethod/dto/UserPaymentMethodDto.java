package com.hart.overwatch.paymentmethod.dto;

public class UserPaymentMethodDto {

    private Long id;

    private String last4;

    private String displayBrand;

    private Long expMonth;

    private Long expYear;

    private String name;

    private Boolean stripeEnabled;

    public UserPaymentMethodDto() {

    }

    public UserPaymentMethodDto(Long id, String last4, String displayBrand, Long expMonth,
            Long expYear, String name, Boolean stripeEnabled) {
        this.id = id;
        this.last4 = last4;
        this.displayBrand = displayBrand;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.name = name;
        this.stripeEnabled = stripeEnabled;
    }


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getLast4() {
        return last4;
    }

    public Boolean getStripeEnabled() {
        return stripeEnabled;
    }

    public Long getExpYear() {
        return expYear;
    }

    public Long getExpMonth() {
        return expMonth;
    }

    public String getDisplayBrand() {
        return displayBrand;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public void setExpYear(Long expYear) {
        this.expYear = expYear;
    }

    public void setExpMonth(Long expMonth) {
        this.expMonth = expMonth;
    }

    public void setDisplayBrand(String displayBrand) {
        this.displayBrand = displayBrand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStripeEnabled(Boolean stripeEnabled) {
        this.stripeEnabled = stripeEnabled;
    }

}
