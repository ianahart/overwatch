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
}
