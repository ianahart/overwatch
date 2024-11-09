package com.hart.overwatch.paymentmethod;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity()
@Table(name = "_user_payment_method")
public class UserPaymentMethod {

    @Id
    @SequenceGenerator(name = "payment_method_sequence", sequenceName = "payment_method_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "city", length = 150)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "line1", length = 200)
    private String line1;

    @Column(name = "line2", length = 00)
    private String line2;

    @Column(name = "postal_code", length = 15)
    private String postalCode;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "display_brand", length = 100)
    private String displayBrand;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "exp_month")
    private Integer expMonth;

    @Column(name = "exp_year")
    private Integer expYear;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_connect_account_id")
    private String stripeConnectAccountId;


    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public UserPaymentMethod() {

    }

    public UserPaymentMethod(Long id, Timestamp createdAt, Timestamp updatedAt, String city,
            String country, String line1, String line2, String postalCode, String name,
            String displayBrand, String type, Integer expMonth, Integer expYear) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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


    public UserPaymentMethod(User user, String city, String country, String line1, String line2,
            String postalCode, String name, String displayBrand, String type, Integer expMonth,
            Integer expYear, String stripeCustomerId) {
        this.user = user;
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
        this.stripeCustomerId = stripeCustomerId;
    }


    public UserPaymentMethod(User user, String stripeConnectAccountId) {
        this.user = user;
        this.stripeConnectAccountId = stripeConnectAccountId;
    }

    public Long getId() {
        return id;
    }

    public String getStripeConnectAccountId() {
        return stripeConnectAccountId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public User getUser() {
        return user;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine1() {
        return line1;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public String getCountry() {
        return country;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getDisplayBrand() {
        return displayBrand;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDisplayBrand(String displayBrand) {
        this.displayBrand = displayBrand;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public void setStripeConnectAccountId(String stripeConnectAccountId) {
        this.stripeConnectAccountId = stripeConnectAccountId;
    }

}


