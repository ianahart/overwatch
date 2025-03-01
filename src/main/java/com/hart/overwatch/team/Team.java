package com.hart.overwatch.team;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.teaminvitation.TeamInvitation;
import com.hart.overwatch.teammember.TeamMember;
import com.hart.overwatch.teammessage.TeamMessage;
import com.hart.overwatch.teampinnedmessage.TeamPinnedMessage;
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

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamPinnedMessage> teamPinnedMessages;


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

    public List<TeamPinnedMessage> getTeamPinnedMessages() {
        return teamPinnedMessages;
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

    public void setTeamPinnedMessages(List<TeamPinnedMessage> teamPinnedMessages) {
        this.teamPinnedMessages = teamPinnedMessages;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
        result = prime * result + ((teamDescription == null) ? 0 : teamDescription.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((teamInvitations == null) ? 0 : teamInvitations.hashCode());
        result = prime * result + ((teamMembers == null) ? 0 : teamMembers.hashCode());
        result = prime * result + ((teamMessages == null) ? 0 : teamMessages.hashCode());
        result = prime * result + ((teamPosts == null) ? 0 : teamPosts.hashCode());
        result = prime * result
                + ((teamPinnedMessages == null) ? 0 : teamPinnedMessages.hashCode());
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
        Team other = (Team) obj;
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
        if (teamName == null) {
            if (other.teamName != null)
                return false;
        } else if (!teamName.equals(other.teamName))
            return false;
        if (teamDescription == null) {
            if (other.teamDescription != null)
                return false;
        } else if (!teamDescription.equals(other.teamDescription))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (teamInvitations == null) {
            if (other.teamInvitations != null)
                return false;
        } else if (!teamInvitations.equals(other.teamInvitations))
            return false;
        if (teamMembers == null) {
            if (other.teamMembers != null)
                return false;
        } else if (!teamMembers.equals(other.teamMembers))
            return false;
        if (teamMessages == null) {
            if (other.teamMessages != null)
                return false;
        } else if (!teamMessages.equals(other.teamMessages))
            return false;
        if (teamPosts == null) {
            if (other.teamPosts != null)
                return false;
        } else if (!teamPosts.equals(other.teamPosts))
            return false;
        if (teamPinnedMessages == null) {
            if (other.teamPinnedMessages != null)
                return false;
        } else if (!teamPinnedMessages.equals(other.teamPinnedMessages))
            return false;
        return true;
    }


}

