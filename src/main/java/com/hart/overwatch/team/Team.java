package com.hart.overwatch.team;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.teaminvitation.TeamInvitation;
import com.hart.overwatch.teammember.TeamMember;
import com.hart.overwatch.teammessage.TeamMessage;
import com.hart.overwatch.teampost.TeamPost;
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

@Entity
@Table(name = "team")
public class Team {

    @Id
    @SequenceGenerator(name = "team_sequence", sequenceName = "team_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "team_name", length = 100)
    private String teamName;

    @Column(name = "team_description", length = 200)
    private String teamDescription;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamInvitation> teamInvitations;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamMessage> teamMessages;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamPost> teamPosts;


    public Team() {

    }

    public Team(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String teamName,
            String teamDescription) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }

    public Team(String teamName, String teamDescription, User user) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<TeamMessage> getTeamMessages() {
        return teamMessages;
    }

    public List<TeamInvitation> getTeamInvitations() {
        return teamInvitations;
    }

    public List<TeamPost> getTeamPosts() {
        return teamPosts;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
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

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public void setTeamInvitations(List<TeamInvitation> teamInvitations) {
        this.teamInvitations = teamInvitations;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void setTeamMessages(List<TeamMessage> teamMessages) {
        this.teamMessages = teamMessages;
    }

    public void setTeamPosts(List<TeamPost> teamPosts) {
        this.teamPosts = teamPosts;
    }
}

