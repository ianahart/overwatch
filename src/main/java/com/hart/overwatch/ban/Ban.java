package com.hart.overwatch.ban;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity()
@Table(name = "ban")
public class Ban {

    @Id
    @SequenceGenerator(name = "ban_sequence", sequenceName = "ban_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ban_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "admin_notes", length = 300)
    private String adminNotes;

    @Column(name = "time")
    private Long time;

    @Column(name = "ban_date")
    private LocalDateTime banDate;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


    public Ban() {

    }

    public Ban(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String adminNotes,
            Long time, LocalDateTime banDate) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminNotes = adminNotes;
        this.time = time;
        this.banDate = banDate;
    }

    public Ban(String adminNotes, Long time, LocalDateTime banDate, User user) {
        this.adminNotes = adminNotes;
        this.time = time;
        this.banDate = banDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public LocalDateTime getBanDate() {
        return banDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setBanDate(LocalDateTime banDate) {
        this.banDate = banDate;
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
        result = prime * result + ((adminNotes == null) ? 0 : adminNotes.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((banDate == null) ? 0 : banDate.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Ban other = (Ban) obj;
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
        if (adminNotes == null) {
            if (other.adminNotes != null)
                return false;
        } else if (!adminNotes.equals(other.adminNotes))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (banDate == null) {
            if (other.banDate != null)
                return false;
        } else if (!banDate.equals(other.banDate))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


}
