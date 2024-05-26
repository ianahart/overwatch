package com.hart.overwatch.profile;

import java.sql.Timestamp;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hart.overwatch.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.Type;



@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @SequenceGenerator(name = "profile_sequence", sequenceName = "profile_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "avatar_filename", length = 400)
    private String avatarFilename;

    @Column(name = "avatar_url", length = 400)
    private String avatarUrl;

    @Type(JsonType.class)
    @Column(name = "work_exp", columnDefinition = "jsonb")
    private Map<String, String> workExp;


    @JsonIgnore
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;

    public Profile() {

    }

    public Profile(Long id, Timestamp createdAt, Timestamp updatedAt, String avatarFilename,
            String avatarUrl, String schoolName, String courses) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.avatarFilename = avatarFilename;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAvatarFilename(String avatarFilename) {
        this.avatarFilename = avatarFilename;
    }



}

