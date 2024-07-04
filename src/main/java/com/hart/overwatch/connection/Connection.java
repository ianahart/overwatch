package com.hart.overwatch.connection;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.connectionpin.ConnectionPin;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity()
@Table(name = "connection")
public class Connection {


    @Id
    @SequenceGenerator(name = "connection_sequence", sequenceName = "connection_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connection_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;


    @ManyToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @OneToMany(mappedBy = "connection", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ChatMessage> chatMessages;

    @OneToMany(mappedBy = "connection", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ConnectionPin> pinnedConnections;


    public Connection() {

    }

    public Connection(Long id, Timestamp createdAt, Timestamp updatedAt, RequestStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public Connection(RequestStatus status, User sender, User receiver) {
        this.status = status;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public User getReceiver() {
        return receiver;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<ConnectionPin> getPinnedConnections() {
        return pinnedConnections;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setPinnedConnections(List<ConnectionPin> pinnedConnections) {
        this.pinnedConnections = pinnedConnections;
    }
}


