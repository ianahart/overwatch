package com.hart.overwatch.checklistitem;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.checklist.CheckList;
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
@Table(name = "check_list_item")
public class CheckListItem {

    @Id
    @SequenceGenerator(name = "check_list_item_sequence", sequenceName = "check_list_item_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "check_list_item_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "check_list_id", referencedColumnName = "id")
    private CheckList checkList;


    public CheckListItem() {

    }

    public CheckListItem(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String title,
            Boolean isCompleted) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public CheckListItem(String title, Boolean isCompleted, User user, CheckList checkList) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.user = user;
        this.checkList = checkList;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public CheckList getCheckList() {
        return checkList;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
