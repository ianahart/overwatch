package com.hart.overwatch.activity;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @SequenceGenerator(name = "activity_sequence", sequenceName = "activity_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "text", length = 200)
    private String text;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "todo_card_id", referencedColumnName = "id")
    private TodoCard todoCard;

    public Activity() {

    }

    public Activity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String text) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.text = text;
    }

    public Activity(String text, User user, TodoCard todoCard) {
        this.text = text;
        this.user = user;
        this.todoCard = todoCard;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public TodoCard getTodoCard() {
        return todoCard;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTodoCard(TodoCard todoCard) {
        this.todoCard = todoCard;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
