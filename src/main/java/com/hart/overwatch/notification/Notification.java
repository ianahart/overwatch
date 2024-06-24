package com.hart.overwatch.notification;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @SequenceGenerator(name = "notification_sequence", sequenceName = "notification_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;


    @Column(name = "text", length = 200, nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_role")
    private NotificationRole notificationRole;

    @ManyToOne()
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @ManyToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = true)
    private User sender;


    public Notification() {

    }

    public Notification(Long id, Timestamp createdAt, Timestamp updatedAt, String text,
            NotificationType notificationType, NotificationRole notificationRole) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.text = text;
        this.notificationType = notificationType;
        this.notificationRole = notificationRole;
    }

    public Notification(String text, NotificationType notificationType, User receiver, User sender,
            NotificationRole notificationRole) {
        this.text = text;
        this.notificationType = notificationType;
        this.receiver = receiver;
        this.sender = sender;
        this.notificationRole = notificationRole;
    }

    public Notification(String text, NotificationType notificationType, User receiver,
            NotificationRole notificationRole) {
        this.text = text;
        this.notificationType = notificationType;
        this.receiver = receiver;
        this.notificationRole = notificationRole;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public NotificationRole getNotificationRole() {
        return notificationRole;
    }

    public String getText() {
        return text;
    }

    public User getReceiver() {
        return receiver;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setNotificationRole(NotificationRole notificationRole) {
        this.notificationRole = notificationRole;
    }
}

