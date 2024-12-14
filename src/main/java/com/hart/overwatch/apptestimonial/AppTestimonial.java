package com.hart.overwatch.apptestimonial;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "app_testimonial")
public class AppTestimonial {

    @Id
    @SequenceGenerator(name = "app_testimonial_sequence", sequenceName = "app_testimonial_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_testimonial_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "developer_type", length = 50)
    private String developerType;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "is_selected")
    private Boolean isSelected;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public AppTestimonial() {

    }

    public AppTestimonial(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
            String developerType, String content, Boolean isSelected) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.developerType = developerType;
        this.content = content;
        this.isSelected = isSelected;
    }

    public AppTestimonial(String developerType, String content, Boolean isSelected, User user) {
        this.developerType = developerType;
        this.content = content;
        this.isSelected = isSelected;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public User getUser() {
        return user;
    }

    public String getDeveloperType() {
        return developerType;
    }

    public String getContent() {
        return content;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeveloperType(String developerType) {
        this.developerType = developerType;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }


}
