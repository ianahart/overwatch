package com.hart.overwatch.user;


import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connectionpin.ConnectionPin;
import com.hart.overwatch.favorite.Favorite;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.notification.Notification;
import com.hart.overwatch.passwordreset.PasswordReset;
import com.hart.overwatch.paymentmethod.UserPaymentMethod;
import com.hart.overwatch.phone.Phone;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.review.Review;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.testimonial.Testimonial;
import com.hart.overwatch.token.Token;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity()
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "_user_sequence", sequenceName = "_user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_user_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "first_name", length = 200)
    private String firstName;

    @Column(name = "last_name", length = 200)
    private String lastName;

    @Column(name = "full_name", length = 400)
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "logged_in")
    private Boolean loggedIn;

    @Transient
    private String abbreviation;

    @Transient
    private String slug;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PasswordReset> passwordResets;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Phone> phones;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserPaymentMethod> userPaymentMethods;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Testimonial> testimonials;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> reviewerReviews;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> authorReviews;


    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Connection> senderConnections;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Connection> receiverConnections;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Notification> receiverNotifications;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Notification> senderNotifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ChatMessage> chatMessages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "pinned", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ConnectionPin> pinnedUserConnections;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ConnectionPin> pinnedOwnerConnections;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "setting_id", referencedColumnName = "id")
    private Setting setting;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Location location;



    public User() {

    }

    public User(Long id, String email, Timestamp createdAt, Timestamp updatedAt, String firstName,
            String lastName, String fullName, Role role, Boolean loggedIn) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.loggedIn = loggedIn;

    }

    public User(String email, String firstName, String lastName, String fullName, Role role,
            Boolean loggedIn, Profile profile, String password, Setting setting) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.loggedIn = loggedIn;
        this.profile = profile;
        this.password = password;
        this.setting = setting;
    }

    public String getAbbreviation() {
        return firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
    }

    public String getSlug() {
        return (firstName + lastName).toLowerCase();
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public List<UserPaymentMethod> getUserPaymentMethods() {
        return userPaymentMethods;
    }

    public Location getLocation() {
        return location;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public List<Connection> getSenderConnections() {
        return senderConnections;
    }

    public List<Connection> getReceiverConnections() {
        return receiverConnections;
    }

    public Long getId() {
        return id;
    }

    public List<Testimonial> getTestimonials() {
        return testimonials;
    }

    public List<Review> getAuthorReviews() {
        return authorReviews;
    }

    public List<Review> getReviewerReviews() {
        return reviewerReviews;
    }

    public List<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public List<PasswordReset> getPasswordResets() {
        return passwordResets;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public List<Token> getTokens() {
        return tokens;
    }


    public Profile getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getFirstName() {
        return firstName;
    }


    public List<ConnectionPin> getPinnedUserConnections() {
        return pinnedUserConnections;
    }

    public List<ConnectionPin> getPinnedOwnerConnections() {
        return pinnedOwnerConnections;
    }

    public List<Notification> getSenderNotifications() {
        return senderNotifications;
    }

    public List<Notification> getReceiverNotifications() {
        return receiverNotifications;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setPinnedUserConnections(List<ConnectionPin> pinnedUserConnections) {
        this.pinnedUserConnections = pinnedUserConnections;
    }

    public void setPinnedOwnerConnections(List<ConnectionPin> pinnedOwnerConnections) {
        this.pinnedOwnerConnections = pinnedOwnerConnections;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public void setTestimonials(List<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setPasswordResets(List<PasswordReset> passwordResets) {
        this.passwordResets = passwordResets;
    }

    public void setUserPaymentMethods(List<UserPaymentMethod> userPaymentMethods) {
        this.userPaymentMethods = userPaymentMethods;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }


    public void setSenderNotifications(List<Notification> senderNotifications) {
        this.senderNotifications = senderNotifications;
    }

    public void setReceiverNotifications(List<Notification> receiverNotifications) {
        this.receiverNotifications = receiverNotifications;
    }

    public void setAuthorReviews(List<Review> authorReviews) {
        this.authorReviews = authorReviews;
    }

    public void setReviewerReviews(List<Review> reviewerReviews) {
        this.reviewerReviews = reviewerReviews;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setRefreshTokens(List<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setSenderConnections(List<Connection> senderConnections) {
        this.senderConnections = senderConnections;
    }

    public void setReceiverConnections(List<Connection> receiverConnections) {
        this.receiverConnections = receiverConnections;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}

