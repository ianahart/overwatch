package com.hart.overwatch.comment;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.commentvote.CommentVote;
import com.hart.overwatch.reaction.Reaction;
import com.hart.overwatch.reaction.dto.ReactionDto;
import com.hart.overwatch.replycomment.ReplyComment;
import com.hart.overwatch.reportcomment.ReportComment;
import com.hart.overwatch.savecomment.SaveComment;
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

    @Column(name = "is_edited")
    private Boolean isEdited;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CommentVote> commentVotes;

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<ReportComment> reportComments;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SaveComment> savedComments;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ReplyComment> replyComments = new ArrayList<>();


    public Comment() {

    }

    public Comment(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String content,
            Boolean isEdited) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.isEdited = isEdited;
    }

    public Comment(String content, Boolean isEdited, User user, Topic topic) {
        this.content = content;
        this.isEdited = isEdited;
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

    public int getReplyCommentsCount() {
        return replyComments.size();
    }

    public List<ReactionDto> getGroupReactionsByComment() {
        return getReactions().stream()
                .collect(Collectors.groupingBy(reaction -> reaction.getEmoji())).entrySet().stream()
                .map(entry -> new ReactionDto(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
    }

    public List<ReplyComment> getReplyComments() {
        return replyComments;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public List<SaveComment> getSavedComments() {
        return savedComments;
    }

    public List<ReportComment> getReportComments() {
        return reportComments;
    }

    public User getUser() {
        return user;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
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

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setReportComments(List<ReportComment> reportComments) {
        this.reportComments = reportComments;
    }

    public void setSavedComments(List<SaveComment> savedComments) {
        this.savedComments = savedComments;
    }

    public void setReplyComments(List<ReplyComment> replyComments) {
        this.replyComments = replyComments;
    }

};
