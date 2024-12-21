package com.hart.overwatch.stripepaymentintent;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.stripepaymentrefund.StripePaymentRefund;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "stripe_payment_intent")
public class StripePaymentIntent {

    @Id
    @SequenceGenerator(name = "stripe_payment_intent_sequence",
            sequenceName = "stripe_payment_intent_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "stripe_payment_intent_sequence")
    @Column(name = "id")
    private Long id;


    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "application_fee")
    private Long applicationFee;

    @Enumerated(EnumType.STRING)
    private PaymentIntentStatus status;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id", nullable = false)
    private User reviewer;

    @OneToOne(mappedBy = "stripePaymentIntent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StripePaymentRefund stripePaymentRefund;

    public StripePaymentIntent() {

    }

    public StripePaymentIntent(String paymentIntentId, User user, User reviewer, Long amount,
            String currency, Long applicationFee, PaymentIntentStatus status, String clientSecret,
            String description) {
        this.paymentIntentId = paymentIntentId;
        this.user = user;
        this.reviewer = reviewer;
        this.amount = amount;
        this.currency = currency;
        this.applicationFee = applicationFee;
        this.status = status;
        this.clientSecret = clientSecret;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Long getApplicationFee() {
        return applicationFee;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PaymentIntentStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(PaymentIntentStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setApplicationFee(Long applicationFee) {
        this.applicationFee = applicationFee;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((applicationFee == null) ? 0 : applicationFee.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((clientSecret == null) ? 0 : clientSecret.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((paymentIntentId == null) ? 0 : paymentIntentId.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((reviewer == null) ? 0 : reviewer.hashCode());
        result = prime * result + ((stripePaymentRefund == null) ? 0 : stripePaymentRefund.hashCode());
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
        StripePaymentIntent other = (StripePaymentIntent) obj;
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
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        } else if (!currency.equals(other.currency))
            return false;
        if (applicationFee == null) {
            if (other.applicationFee != null)
                return false;
        } else if (!applicationFee.equals(other.applicationFee))
            return false;
        if (status != other.status)
            return false;
        if (clientSecret == null) {
            if (other.clientSecret != null)
                return false;
        } else if (!clientSecret.equals(other.clientSecret))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (paymentIntentId == null) {
            if (other.paymentIntentId != null)
                return false;
        } else if (!paymentIntentId.equals(other.paymentIntentId))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (reviewer == null) {
            if (other.reviewer != null)
                return false;
        } else if (!reviewer.equals(other.reviewer))
            return false;
        if (stripePaymentRefund == null) {
            if (other.stripePaymentRefund != null)
                return false;
        } else if (!stripePaymentRefund.equals(other.stripePaymentRefund))
            return false;
        return true;
    }


}
