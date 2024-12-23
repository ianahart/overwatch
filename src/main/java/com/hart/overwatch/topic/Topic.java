package com.hart.overwatch.topic;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "topic")
public class Topic {

    @Id
    @SequenceGenerator(name = "topic_sequence", sequenceName = "topic_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "description", length = 250, nullable = false)
    private String description;

    @Column(name = "search_vector", insertable = false, updatable = false,
            columnDefinition = "tsvector")
    private String searchVector;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "topic_tags", joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();


    public Topic() {

    }

    public Topic(Long id, LocalDateTime createdAt, String title, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
    }

    public Topic(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSearchVector() {
        return searchVector;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setSearchVector(String searchVector) {
        this.searchVector = searchVector;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTopics().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getTopics().remove(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((searchVector == null) ? 0 : searchVector.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
        Topic other = (Topic) obj;
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
        if (searchVector == null) {
            if (other.searchVector != null)
                return false;
        } else if (!searchVector.equals(other.searchVector))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        return true;
    }


}
