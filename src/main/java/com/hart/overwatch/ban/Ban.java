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
}
