package com.hart.overwatch.token;



import com.hart.overwatch.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "token")
public class Token {

    @Id
    @SequenceGenerator(name = "token_sequence", sequenceName = "token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_sequence")
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Column(name = "expired")
    private Boolean expired;
    @Column(name = "revoked")
    private Boolean revoked;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Token() {}

    public Token(Long id, String token, TokenType tokenType, Boolean expired, Boolean revoked) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
    }

    public Token(Long id, String token, TokenType tokenType, Boolean expired, Boolean revoked,
            User user) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }

    public Token(String token, TokenType tokenType, Boolean expired, Boolean revoked, User user) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Boolean getExpired() {
        return expired;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
        result = prime * result + ((expired == null) ? 0 : expired.hashCode());
        result = prime * result + ((revoked == null) ? 0 : revoked.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Token other = (Token) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        if (tokenType != other.tokenType)
            return false;
        if (expired == null) {
            if (other.expired != null)
                return false;
        } else if (!expired.equals(other.expired))
            return false;
        if (revoked == null) {
            if (other.revoked != null)
                return false;
        } else if (!revoked.equals(other.revoked))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


}
