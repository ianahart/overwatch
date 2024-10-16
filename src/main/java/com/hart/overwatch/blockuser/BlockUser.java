package com.hart.overwatch.blockuser;

import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "block_user")
public class BlockUser {

    @Id
    @SequenceGenerator(name = "block_user_sequence", sequenceName = "block_user_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_user_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private User blockedUser;

    @ManyToOne()
    @JoinColumn(name = "blocker_user_id", referencedColumnName = "id")
    private User blockerUser;

    public BlockUser() {

    }

    public BlockUser(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BlockUser(User blockerUser, User blockedUser) {
        this.blockerUser = blockedUser;
        this.blockerUser = blockedUser;
    }

    public Long getId() {
        return id;
    }

    public User getBlockedUser() {
        return blockedUser;
    }

    public User getBlockerUser() {
        return blockerUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBlockedUser(User blockedUser) {
        this.blockedUser = blockedUser;
    }

    public void setBlockerUser(User blockerUser) {
        this.blockerUser = blockerUser;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
