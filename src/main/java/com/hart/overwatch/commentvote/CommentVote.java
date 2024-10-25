package com.hart.overwatch.commentvote;

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
@Table(name = "comment_vote")
public class CommentVote {

    @Id
    @SequenceGenerator(name = "comment_vote_sequence", sequenceName = "comment_vote_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_vote_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "vote_type", nullable = false)
    private String voteType;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment comment;

    public CommentVote() {}

    public CommentVote(Long id, LocalDateTime createdAt, String voteType) {
        this.id = id;
        this.createdAt = createdAt;
        this.voteType = voteType;
    }

    public CommentVote(Comment comment, User user, String voteType) {
        this.comment = comment;
        this.user = user;
        this.voteType = voteType;
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Comment getComment() {
        return comment;
    }

    public String getVoteType() {
        return voteType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

