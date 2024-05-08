package com.hart.overwatch.passwordreset;

import java.sql.Timestamp;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
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
@Table(name = "password_reset")
public class PasswordReset {

    @Id
    @SequenceGenerator(name = "password_reset_sequence", sequenceName = "password_reset_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "token")
    private String token;

    @Column(name = "code")
    private String code;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public PasswordReset() {

    }

    public PasswordReset(Long id, Timestamp createdAt, String token, String code) {
        this.id = id;
        this.createdAt = createdAt;
        this.token = token;
        this.code = code;
    }

    public PasswordReset(User user, String code, String token) {
        this.user = user;
        this.code = code;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
