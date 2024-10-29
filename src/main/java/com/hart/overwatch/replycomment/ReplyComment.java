package com.hart.overwatch.replycomment;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.hart.overwatch.comment.Comment;
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
@Table(name = "reply_comment")
public class ReplyComment {

    @Id
    @SequenceGenerator(name = "reply_comment_sequence", sequenceName = "reply_comment_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_comment_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited;

    @Column(name = "content", length = 400, nullable = false)
    private String content;

    @ManyToOne()
    @JoinColumn(name = "comment_id", nullable = false, referencedColumnName = "id")
    private Comment comment;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    public ReplyComment() {

    }

    public ReplyComment(Long id, LocalDateTime createdAt, Boolean isEdited, String content) {
        this.id = id;
        this.createdAt = createdAt;
        this.isEdited = isEdited;
        this.content = content;
    }

    public ReplyComment(Boolean isEdited, String content, User user, Comment comment) {
        this.isEdited = isEdited;
        this.content = content;
        this.user = user;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public Comment getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }



}
