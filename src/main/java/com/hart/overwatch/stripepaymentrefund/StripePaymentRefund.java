package com.hart.overwatch.stripepaymentrefund;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "stripe_payment_refund")
public class StripePaymentRefund {

    @Id
    @SequenceGenerator(name = "stripe_payment_refund_sequence",
            sequenceName = "stripe_payment_refund_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "stripe_payment_refund_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "refund_id")
    private String refundId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "stripe_payment_intent_id", referencedColumnName = "id")
    private StripePaymentIntent stripePaymentIntent;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "reason", columnDefinition = "TEXT", length = 1000)
    private String reason;

    @Column(name = "admin_notes", length = 300)
    private String adminNotes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private PaymentRefundStatus status;


    public StripePaymentRefund() {

    }

    public StripePaymentRefund(StripePaymentIntent stripePaymentIntent, User user, Long amount,
            String currency, String reason, PaymentRefundStatus status) {
        this.stripePaymentIntent = stripePaymentIntent;
        this.user = user;
        this.amount = amount;
        this.currency = currency;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PaymentRefundStatus getStatus() {
        return status;
    }

    public StripePaymentIntent getStripePaymentIntent() {
        return stripePaymentIntent;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(PaymentRefundStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStripePaymentIntent(StripePaymentIntent stripePaymentIntent) {
        this.stripePaymentIntent = stripePaymentIntent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((refundId == null) ? 0 : refundId.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((stripePaymentIntent == null) ? 0 : stripePaymentIntent.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((adminNotes == null) ? 0 : adminNotes.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        StripePaymentRefund other = (StripePaymentRefund) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (refundId == null) {
            if (other.refundId != null)
                return false;
        } else if (!refundId.equals(other.refundId))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (stripePaymentIntent == null) {
            if (other.stripePaymentIntent != null)
                return false;
        } else if (!stripePaymentIntent.equals(other.stripePaymentIntent))
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
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        if (adminNotes == null) {
            if (other.adminNotes != null)
                return false;
        } else if (!adminNotes.equals(other.adminNotes))
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
        if (status != other.status)
            return false;
        return true;
    }



}
