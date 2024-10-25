package com.hart.overwatch.comment;

import java.util.List;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.commentvote.CommentVote;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "comment")
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "content", length = 400, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CommentVote> commentVotes;


    public Comment() {

    }

    public Comment(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String content) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
    }

    public Comment(String content, User user, Topic topic) {
        this.content = content;
        this.user = user;
        this.topic = topic;
    }

    public int getUpVoteCount() {
        return (int) commentVotes.stream().filter(cv -> "UPVOTE".equals(cv.getVoteType())).count();
    }

    public int getDownVoteCount() {
        return (int) commentVotes.stream().filter(cv -> "DOWNVOTE".equals(cv.getVoteType()))
                .count();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<CommentVote> getCommentVotes() {
        return commentVotes;
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

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public void setCommentVotes(List<CommentVote> commentVotes) {
        this.commentVotes = commentVotes;
    }

};
