package com.hart.overwatch.setting.dto;

import java.sql.Timestamp;

public class SettingDto {

    private Long id;

    private Long userId;

    private Boolean mfaEnabled;

    private Timestamp createdAt;

    private Boolean reviewInProgressNotifOn;

    private Boolean reviewInCompleteNotifOn;

    private Boolean reviewCompletedNotifOn;

    private Boolean paymentAcknowledgementNotifOn;

    private Boolean requestPendingNotifOn;

    private Boolean requestAcceptedNotifOn;

    private Boolean commentReplyOn;

    private Boolean emailOn;


    public SettingDto() {

    }

    public SettingDto(Long id, Long userId, Boolean mfaEnabled, Timestamp createdAt,
            Boolean reviewInProgressNotifOn, Boolean reviewInCompleteNotifOn,
            Boolean reviewCompletedNotifOn, Boolean paymentAcknowledgementNotifOn,
            Boolean requestPendingNotifOn, Boolean requestAcceptedNotifOn, Boolean commentReplyOn,
            Boolean emailOn) {
        this.id = id;
        this.userId = userId;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
        this.reviewInProgressNotifOn = reviewInProgressNotifOn;
        this.reviewInCompleteNotifOn = reviewInCompleteNotifOn;
        this.reviewCompletedNotifOn = reviewCompletedNotifOn;
        this.paymentAcknowledgementNotifOn = paymentAcknowledgementNotifOn;
        this.requestPendingNotifOn = paymentAcknowledgementNotifOn;
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

    public Boolean getRequestPendingNotifOn() {
        return requestPendingNotifOn;
    }

    public Boolean getCommentReplyOn() {
        return commentReplyOn;
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

    public Boolean getPaymentAcknowledgementNotifOn() {
        return paymentAcknowledgementNotifOn;
    }

    public Boolean getReviewInProgressNotifOn() {
        return reviewInProgressNotifOn;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
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

    public void setEmailOn(Boolean emailOn) {
        this.emailOn = emailOn;
    }
}
