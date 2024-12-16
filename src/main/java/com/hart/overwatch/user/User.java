package com.hart.overwatch.user;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.hart.overwatch.activity.Activity;
import com.hart.overwatch.apptestimonial.AppTestimonial;
import com.hart.overwatch.ban.Ban;
import com.hart.overwatch.blockuser.BlockUser;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.checklistitem.CheckListItem;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.commentvote.CommentVote;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connectionpin.ConnectionPin;
import com.hart.overwatch.customfield.CustomField;
import com.hart.overwatch.favorite.Favorite;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.location.Location;
import com.hart.overwatch.notification.Notification;
import com.hart.overwatch.passwordreset.PasswordReset;
import com.hart.overwatch.paymentmethod.UserPaymentMethod;
import com.hart.overwatch.phone.Phone;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reaction.Reaction;
import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.replycomment.ReplyComment;
import com.hart.overwatch.reportcomment.ReportComment;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.review.Review;
import com.hart.overwatch.reviewfeedback.ReviewFeedback;
import com.hart.overwatch.savecomment.SaveComment;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.suggestion.Suggestion;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.teamcomment.TeamComment;
import com.hart.overwatch.teaminvitation.TeamInvitation;
import com.hart.overwatch.teammember.TeamMember;
import com.hart.overwatch.teammessage.TeamMessage;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.testimonial.Testimonial;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.token.Token;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.workspace.WorkSpace;
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

    @Column(name = "last_active")
    private LocalDateTime lastActive;

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

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Repository> reviewerRepositories;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Repository> ownerRepositories;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<WorkSpace> workSpaces;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TodoList> todoLists;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TodoCard> todocards;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Label> labels;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CheckList> checkLists;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CheckListItem> checkListItems;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Activity> activities;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CustomField> customFields;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ReviewFeedback> ownerReviewFeedbacks;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ReviewFeedback> ReviewerReviewFeedbacks;

    @OneToMany(mappedBy = "blockedUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BlockUser> blockedUsers;

    @OneToMany(mappedBy = "blockerUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BlockUser> blockerUsers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Topic> topics;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CommentVote> commentVotes;

    @OneToMany(mappedBy = "reportedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ReportComment> reportedComments;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SaveComment> savedComments;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ReplyComment> replyComments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StripePaymentIntent> userStripePaymentIntents;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StripePaymentIntent> reviewierStripePaymentIntents;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AppTestimonial> appTestimonials;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Team> teams;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamInvitation> senderTeamInvitations;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamInvitation> receiverTeamInvitations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamMessage> teamMessages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamPost> teamPosts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeamComment> teamComments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Suggestion> suggestions;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "setting_id", referencedColumnName = "id")
    private Setting setting;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Ban ban;


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

    public List<ReplyComment> getReplyComments() {
        return replyComments;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public List<TeamPost> getTeamPosts() {
        return teamPosts;
    }

    public List<TeamComment> getTeamComments() {
        return teamComments;
    }

    public Ban getBan() {
        return ban;
    }

    public List<TeamMessage> getTeamMessages() {
        return teamMessages;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public String getSlug() {
        return (firstName + lastName).toLowerCase();
    }

    public List<SaveComment> getSavedComments() {
        return savedComments;
    }

    public List<AppTestimonial> getAppTestimonials() {
        return appTestimonials;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public List<BlockUser> getBlockedUsers() {
        return blockedUsers;
    }

    public List<ReportComment> getReportedComments() {
        return reportedComments;
    }

    public List<Comment> getComments() {
        return comments;
    }


    public List<TeamInvitation> getReceiverTeamInvitations() {
        return receiverTeamInvitations;
    }

    public List<TeamInvitation> getSenderTeamInvitations() {
        return senderTeamInvitations;
    }

    public List<BlockUser> getBlockerUsers() {
        return blockerUsers;
    }

    public List<CommentVote> getCommentVotes() {
        return commentVotes;
    }

    public List<CheckList> getCheckLists() {
        return checkLists;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public List<CheckListItem> getCheckListItems() {
        return checkListItems;
    }

    public List<ReviewFeedback> getOwnerReviewFeedbacks() {
        return ownerReviewFeedbacks;
    }

    public List<ReviewFeedback> getReviewerReviewFeedbacks() {
        return ReviewerReviewFeedbacks;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public List<UserPaymentMethod> getUserPaymentMethods() {
        return userPaymentMethods;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public Location getLocation() {
        return location;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public List<Connection> getSenderConnections() {
        return senderConnections;
    }

    public List<TodoCard> getTodocards() {
        return todocards;
    }

    public List<TodoList> getTodoLists() {
        return todoLists;
    }

    public List<Repository> getOwnerRepositories() {
        return ownerRepositories;
    }

    public List<Repository> getReviewerRepositories() {
        return reviewerRepositories;
    }

    public List<WorkSpace> getWorkSpaces() {
        return workSpaces;
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

    public List<StripePaymentIntent> getUserStripePaymentIntents() {
        return userStripePaymentIntents;
    }

    public List<StripePaymentIntent> getReviewierStripePaymentIntents() {
        return reviewierStripePaymentIntents;
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

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setTodoLists(List<TodoList> todoLists) {
        this.todoLists = todoLists;
    }

    public void setCheckListItems(List<CheckListItem> checkListItems) {
        this.checkListItems = checkListItems;
    }

    public void setSavedComments(List<SaveComment> savedComments) {
        this.savedComments = savedComments;
    }

    public void setPinnedOwnerConnections(List<ConnectionPin> pinnedOwnerConnections) {
        this.pinnedOwnerConnections = pinnedOwnerConnections;
    }

    public void setOwnerReviewFeedbacks(List<ReviewFeedback> ownerReviewFeedbacks) {
        this.ownerReviewFeedbacks = ownerReviewFeedbacks;
    }

    public void setReviewerReviewFeedbacks(List<ReviewFeedback> reviewerReviewFeedbacks) {
        ReviewerReviewFeedbacks = reviewerReviewFeedbacks;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void setWorkSpaces(List<WorkSpace> workSpaces) {
        this.workSpaces = workSpaces;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOwnerRepositories(List<Repository> ownerRepositories) {
        this.ownerRepositories = ownerRepositories;
    }

    public void setTeamMessages(List<TeamMessage> teamMessages) {
        this.teamMessages = teamMessages;
    }

    public void setReviewerRepositories(List<Repository> reviewerRepositories) {
        this.reviewerRepositories = reviewerRepositories;
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

    public void setCommentVotes(List<CommentVote> commentVotes) {
        this.commentVotes = commentVotes;
    }

    public void setTodocards(List<TodoCard> todocards) {
        this.todocards = todocards;
    }

    public void setBlockedUsers(List<BlockUser> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public void setBlockerUsers(List<BlockUser> blockerUsers) {
        this.blockerUsers = blockerUsers;
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

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public void setReplyComments(List<ReplyComment> replyComments) {
        this.replyComments = replyComments;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public void setReportedComments(List<ReportComment> reportedComments) {
        this.reportedComments = reportedComments;
    }

    public void setUserStripePaymentIntents(List<StripePaymentIntent> userStripePaymentIntents) {
        this.userStripePaymentIntents = userStripePaymentIntents;
    }

    public void setSenderTeamInvitations(List<TeamInvitation> senderTeamInvitations) {
        this.senderTeamInvitations = senderTeamInvitations;
    }

    public void setReceiverTeamInvitations(List<TeamInvitation> receiverTeamInvitations) {
        this.receiverTeamInvitations = receiverTeamInvitations;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public void setAppTestimonials(List<AppTestimonial> appTestimonials) {
        this.appTestimonials = appTestimonials;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void setTeamPosts(List<TeamPost> teamPosts) {
        this.teamPosts = teamPosts;
    }

    public void setReviewierStripePaymentIntents(
            List<StripePaymentIntent> reviewierStripePaymentIntents) {
        this.reviewierStripePaymentIntents = reviewierStripePaymentIntents;
    }

    public void setTeamComments(List<TeamComment> teamComments) {
        this.teamComments = teamComments;
    }

    public String getUsername() {
        return email;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
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

