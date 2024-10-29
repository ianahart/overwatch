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

    @JsonIgnore
    @OneToOne(mappedBy = "setting")
    private User user;

    public Setting() {

    }

    public Setting(Long id, Timestamp createdAt, Timestamp updatedAt, Boolean mfaEnabled,
            Boolean reviewInProgressNotifOn, Boolean reviewInCompleteNotifOn,
            Boolean reviewCompletedNotifOn, Boolean paymentAcknowledgementNotifOn,
            Boolean requestPendingNotifOn, Boolean requestAcceptedNotifOn, Boolean commentReplyOn) {
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
    }


    public Setting(Boolean reviewInProgressNotifOn, Boolean reviewInCompleteNotifOn,
            Boolean reviewCompletedNotifOn, Boolean paymentAcknowledgementNotifOn,
            Boolean requestPendingNotifOn, Boolean requestAcceptedNotifOn, Boolean commentReplyOn) {
        this.reviewInProgressNotifOn = reviewInProgressNotifOn;
        this.reviewInCompleteNotifOn = reviewInCompleteNotifOn;
        this.reviewCompletedNotifOn = reviewCompletedNotifOn;
        this.paymentAcknowledgementNotifOn = paymentAcknowledgementNotifOn;
        this.requestPendingNotifOn = requestPendingNotifOn;
        this.requestAcceptedNotifOn = requestAcceptedNotifOn;
        this.commentReplyOn = commentReplyOn;
    }


    public Long getId() {
        return id;
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

}

