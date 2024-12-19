package com.hart.overwatch.badge;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity()
@Table(name = "badge")
public class Badge {

    @Id
    @SequenceGenerator(name = "badge_sequence", sequenceName = "badge_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "badge_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_filename")
    private String imageFileName;

    @Column(name = "image_url", length = 400)
    private String imageUrl;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "description", length = 200)
    private String description;

    public Badge() {

    }

    public Badge(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String imageFileName,
            String imageUrl, String title, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageFileName = imageFileName;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public Badge(String imageFileName, String imageUrl, String title, String description) {
        this.imageFileName = imageFileName;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageFileName() {
        return imageFileName;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
