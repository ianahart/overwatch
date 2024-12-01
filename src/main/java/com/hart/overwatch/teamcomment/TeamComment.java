package com.hart.overwatch.teamcomment;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity()
@Table(name = "team_comment")
public class TeamComment {

    @Id
    @SequenceGenerator(name = "team_comment_sequence", sequenceName = "team_comment_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_comment_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @Column(name = "tag", length = 200, nullable = true)
    private String tag;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "team_post_id", referencedColumnName = "id", nullable = false)
    private TeamPost teamPost;

    public TeamComment() {

    }

    public TeamComment(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String content,
            Boolean isEdited, String tag) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.isEdited = isEdited;
        this.tag = tag;
    }

    public TeamComment(Boolean isEdited, String content, User user, TeamPost teamPost, String tag) {
        this.isEdited = isEdited;
        this.content = content;
        this.user = user;
        this.teamPost = teamPost;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public TeamPost getTeamPost() {
        return teamPost;
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

    public void setTeamPost(TeamPost teamPost) {
        this.teamPost = teamPost;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
