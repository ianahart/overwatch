package com.hart.overwatch.teampost;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.team.Team;
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


@Entity
@Table(name = "team_post")
public class TeamPost {

    @Id
    @SequenceGenerator(name = "team_post_sequence", sequenceName = "team_post_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_post_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "code", length = 600)
    private String code;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "language", length = 100)
    private String language;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = false)
    private Team team;


    public TeamPost() {

    }

    public TeamPost(String code, String language, Boolean isEdited, User user, Team team) {
        this.code = code;
        this.language = language;
        this.isEdited = isEdited;
        this.user = user;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public Team getTeam() {
        return team;
    }

    public User getUser() {
        return user;
    }

    public String getCode() {
        return code;
    }

    public Boolean getIsEdited() {
        return isEdited;
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

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void setLanguage(String language) {
        this.language = language;
    }

}
