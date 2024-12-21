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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((blockedUser == null) ? 0 : blockedUser.hashCode());
        result = prime * result + ((blockerUser == null) ? 0 : blockerUser.hashCode());
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
        BlockUser other = (BlockUser) obj;
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
        if (blockedUser == null) {
            if (other.blockedUser != null)
                return false;
        } else if (!blockedUser.equals(other.blockedUser))
            return false;
        if (blockerUser == null) {
            if (other.blockerUser != null)
                return false;
        } else if (!blockerUser.equals(other.blockerUser))
            return false;
        return true;
    }


}
