package com.hart.overwatch.setting;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hart.overwatch.user.User;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "setting")
public class Setting {

    @Id
    @SequenceGenerator(name = "setting_sequence", sequenceName = "setting_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled;

    @Column(name = "review_inprogress_notif_on")
    private Boolean reviewInProgressNotifOn;

    @Column(name = "review_incomplete_notif_on")
    private Boolean reviewInCompleteNotifOn;

    @Column(name = "review_completed_notif_on")
    private Boolean reviewCompletedNotifOn;

    @Column(name = "payment_acknowledgement_notif_on")
    private Boolean paymentAcknowledgementNotifOn;

    @Column(name = "request_pending_notif_on")
    private Boolean requestPendingNotifOn;

    @Column(name = "request_accepted_notif_on")
    private Boolean requestAcceptedNotifOn;

    @Column(name = "comment_reply_on")
    private Boolean commentReplyOn;

    @Column(name = "email_on", nullable = true)
    private Boolean emailOn;

    @JsonIgnore
    @OneToOne(mappedBy = "setting")
    private User user;

    public Setting() {

    }

    public Setting(Long id, Timestamp createdAt, Timestamp updatedAt, Boolean mfaEnabled,
            Boolean reviewInProgressNotifOn, Boolean reviewInCompleteNotifOn,
            Boolean reviewCompletedNotifOn, Boolean paymentAcknowledgementNotifOn,
            Boolean requestPendingNotifOn, Boolean requestAcceptedNotifOn, Boolean commentReplyOn,
            Boolean emailOn) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.mfaEnabled = mfaEnabled;
        this.reviewInProgressNotifOn = reviewInProgressNotifOn;
        this.reviewInCompleteNotifOn = reviewInCompleteNotifOn;
        this.reviewCompletedNotifOn = reviewCompletedNotifOn;
        this.paymentAcknowledgementNotifOn = paymentAcknowledgementNotifOn;
        this.requestPendingNotifOn = requestPendingNotifOn;
        this.requestAcceptedNotifOn = requestAcceptedNotifOn;
        this.commentReplyOn = commentReplyOn;
        this.emailOn = emailOn;
    }


    public Setting(Boolean reviewInProgressNotifOn, Boolean reviewInCompleteNotifOn,
            Boolean reviewCompletedNotifOn, Boolean paymentAcknowledgementNotifOn,
            Boolean requestPendingNotifOn, Boolean requestAcceptedNotifOn, Boolean commentReplyOn,
            Boolean emailOn) {
        this.reviewInProgressNotifOn = reviewInProgressNotifOn;
        this.reviewInCompleteNotifOn = reviewInCompleteNotifOn;
        this.reviewCompletedNotifOn = reviewCompletedNotifOn;
        this.paymentAcknowledgementNotifOn = paymentAcknowledgementNotifOn;
        this.requestPendingNotifOn = requestPendingNotifOn;
        this.requestAcceptedNotifOn = requestAcceptedNotifOn;
        this.commentReplyOn = commentReplyOn;
        this.emailOn = emailOn;
    }


    public Long getId() {
        return id;
    }

    public Boolean getEmailOn() {
        return emailOn;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public Boolean getRequestPendingNotifOn() {
        return requestPendingNotifOn;
    }

    public Boolean getRequestAcceptedNotifOn() {
        return requestAcceptedNotifOn;
    }

    public Boolean getReviewCompletedNotifOn() {
        return reviewCompletedNotifOn;
    }

    public Boolean getReviewInCompleteNotifOn() {
        return reviewInCompleteNotifOn;
    }

    public Boolean getReviewInProgressNotifOn() {
        return reviewInProgressNotifOn;
    }

    public Boolean getPaymentAcknowledgementNotifOn() {
        return paymentAcknowledgementNotifOn;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getCommentReplyOn() {
        return commentReplyOn;
    }

    public User getUser() {
        return user;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setEmailOn(Boolean emailOn) {
        this.emailOn = emailOn;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRequestPendingNotifOn(Boolean requestPendingNotifOn) {
        this.requestPendingNotifOn = requestPendingNotifOn;
    }

    public void setRequestAcceptedNotifOn(Boolean requestAcceptedNotifOn) {
        this.requestAcceptedNotifOn = requestAcceptedNotifOn;
    }

    public void setReviewCompletedNotifOn(Boolean reviewCompletedNotifOn) {
        this.reviewCompletedNotifOn = reviewCompletedNotifOn;
    }

    public void setReviewInCompleteNotifOn(Boolean reviewInCompleteNotifOn) {
        this.reviewInCompleteNotifOn = reviewInCompleteNotifOn;
    }

    public void setReviewInProgressNotifOn(Boolean reviewInProgressNotifOn) {
        this.reviewInProgressNotifOn = reviewInProgressNotifOn;
    }

    public void setPaymentAcknowledgementNotifOn(Boolean paymentAcknowledgementNotifOn) {
        this.paymentAcknowledgementNotifOn = paymentAcknowledgementNotifOn;
    }

    public void setCommentReplyOn(Boolean commentReplyOn) {
        this.commentReplyOn = commentReplyOn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((mfaEnabled == null) ? 0 : mfaEnabled.hashCode());
        result = prime * result
                + ((reviewInProgressNotifOn == null) ? 0 : reviewInProgressNotifOn.hashCode());
        result = prime * result
                + ((reviewInCompleteNotifOn == null) ? 0 : reviewInCompleteNotifOn.hashCode());
        result = prime * result
                + ((reviewCompletedNotifOn == null) ? 0 : reviewCompletedNotifOn.hashCode());
        result = prime * result + ((paymentAcknowledgementNotifOn == null) ? 0
                : paymentAcknowledgementNotifOn.hashCode());
        result = prime * result
                + ((requestPendingNotifOn == null) ? 0 : requestPendingNotifOn.hashCode());
        result = prime * result
                + ((requestAcceptedNotifOn == null) ? 0 : requestAcceptedNotifOn.hashCode());
        result = prime * result + ((emailOn == null) ? 0 : emailOn.hashCode());
        result = prime * result + ((commentReplyOn == null) ? 0 : commentReplyOn.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Setting other = (Setting) obj;

        if (id == null ? other.id != null : !id.equals(other.id))
            return false;
        if (createdAt == null ? other.createdAt != null : !createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null ? other.updatedAt != null : !updatedAt.equals(other.updatedAt))
            return false;
        if (mfaEnabled == null ? other.mfaEnabled != null : !mfaEnabled.equals(other.mfaEnabled))
            return false;
        if (reviewInProgressNotifOn == null ? other.reviewInProgressNotifOn != null
                : !reviewInProgressNotifOn.equals(other.reviewInProgressNotifOn))
            return false;
        if (reviewInCompleteNotifOn == null ? other.reviewInCompleteNotifOn != null
                : !reviewInCompleteNotifOn.equals(other.reviewInCompleteNotifOn))
            return false;
        if (reviewCompletedNotifOn == null ? other.reviewCompletedNotifOn != null
                : !reviewCompletedNotifOn.equals(other.reviewCompletedNotifOn))
            return false;
        if (paymentAcknowledgementNotifOn == null ? other.paymentAcknowledgementNotifOn != null
                : !paymentAcknowledgementNotifOn.equals(other.paymentAcknowledgementNotifOn))
            return false;
        if (requestPendingNotifOn == null ? other.requestPendingNotifOn != null
                : !requestPendingNotifOn.equals(other.requestPendingNotifOn))
            return false;
        if (requestAcceptedNotifOn == null ? other.requestAcceptedNotifOn != null
                : !requestAcceptedNotifOn.equals(other.requestAcceptedNotifOn))
            return false;
        if (commentReplyOn == null ? other.commentReplyOn != null
                : !commentReplyOn.equals(other.commentReplyOn))
            return false;
        if (user == null ? other.user != null : !user.equals(other.user))
            return false;
        if (emailOn == null ? other.emailOn != null : !emailOn.equals(other.emailOn))
            return false;

        return true;
    }


}

