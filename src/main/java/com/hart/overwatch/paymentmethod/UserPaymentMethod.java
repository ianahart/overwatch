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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((line1 == null) ? 0 : line1.hashCode());
        result = prime * result + ((line2 == null) ? 0 : line2.hashCode());
        result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((displayBrand == null) ? 0 : displayBrand.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((expMonth == null) ? 0 : expMonth.hashCode());
        result = prime * result + ((expYear == null) ? 0 : expYear.hashCode());
        result = prime * result + ((stripeCustomerId == null) ? 0 : stripeCustomerId.hashCode());
        result = prime * result + ((stripeConnectAccountId == null) ? 0 : stripeConnectAccountId.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserPaymentMethod other = (UserPaymentMethod) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (line1 == null) {
            if (other.line1 != null)
                return false;
        } else if (!line1.equals(other.line1))
            return false;
        if (line2 == null) {
            if (other.line2 != null)
                return false;
        } else if (!line2.equals(other.line2))
            return false;
        if (postalCode == null) {
            if (other.postalCode != null)
                return false;
        } else if (!postalCode.equals(other.postalCode))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (displayBrand == null) {
            if (other.displayBrand != null)
                return false;
        } else if (!displayBrand.equals(other.displayBrand))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (expMonth == null) {
            if (other.expMonth != null)
                return false;
        } else if (!expMonth.equals(other.expMonth))
            return false;
        if (expYear == null) {
            if (other.expYear != null)
                return false;
        } else if (!expYear.equals(other.expYear))
            return false;
        if (stripeCustomerId == null) {
            if (other.stripeCustomerId != null)
                return false;
        } else if (!stripeCustomerId.equals(other.stripeCustomerId))
            return false;
        if (stripeConnectAccountId == null) {
            if (other.stripeConnectAccountId != null)
                return false;
        } else if (!stripeConnectAccountId.equals(other.stripeConnectAccountId))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


}


