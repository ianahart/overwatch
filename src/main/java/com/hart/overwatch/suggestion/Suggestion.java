package com.hart.overwatch.suggestion;

import java.time.LocalDateTime;
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
@Table(name = "suggestion")
public class Suggestion {

    @Id
    @SequenceGenerator(name = "suggestion_sequence", sequenceName = "suggestion_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suggestion_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title", length = 150)
    private String title;

    @Column(name = "description", length = 600)
    private String description;

    @Column(name = "contact", length = 150)
    private String contact;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url", length = 400)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Enumerated(EnumType.STRING)
    private PriorityLevel priorityLevel;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus feedbackStatus;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Suggestion() {

    }

    public Suggestion(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String title,
            String description, String contact, String fileName, String fileUrl,
            FeedbackType feedbackType, PriorityLevel priorityLevel, FeedbackStatus feedbackStatus) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.feedbackType = feedbackType;
        this.priorityLevel = priorityLevel;
        this.feedbackStatus = feedbackStatus;
    }

    public Suggestion(String title, String description, String contact, String fileName,
            String fileUrl, FeedbackType feedbackType, PriorityLevel priorityLevel,
            FeedbackStatus feedbackStatus, User user) {
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.feedbackType = feedbackType;
        this.priorityLevel = priorityLevel;
        this.feedbackStatus = feedbackStatus;
        this.user = user;
    }

    public Suggestion(String title, String description, String contact, FeedbackType feedbackType,
            PriorityLevel priorityLevel, FeedbackStatus feedbackStatus, User user) {
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.feedbackType = feedbackType;
        this.priorityLevel = priorityLevel;
        this.feedbackStatus = feedbackStatus;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContact() {
        return contact;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((contact == null) ? 0 : contact.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((fileUrl == null) ? 0 : fileUrl.hashCode());
        result = prime * result + ((feedbackType == null) ? 0 : feedbackType.hashCode());
        result = prime * result + ((priorityLevel == null) ? 0 : priorityLevel.hashCode());
        result = prime * result + ((feedbackStatus == null) ? 0 : feedbackStatus.hashCode());
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
        Suggestion other = (Suggestion) obj;
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
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (contact == null) {
            if (other.contact != null)
                return false;
        } else if (!contact.equals(other.contact))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (fileUrl == null) {
            if (other.fileUrl != null)
                return false;
        } else if (!fileUrl.equals(other.fileUrl))
            return false;
        if (feedbackType != other.feedbackType)
            return false;
        if (priorityLevel != other.priorityLevel)
            return false;
        if (feedbackStatus != other.feedbackStatus)
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


}
