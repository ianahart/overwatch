package com.hart.overwatch.checklist;

import java.util.List;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.checklistitem.CheckListItem;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "check_list")
public class CheckList {

    @Id
    @SequenceGenerator(name = "check_list_sequence", sequenceName = "check_list_sequence",

            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "check_list_sequence")
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
    @JoinColumn(name = "todo_card_id", referencedColumnName = "id")
    private TodoCard todoCard;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "checkList", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CheckListItem> checkListItems;


    public CheckList() {

    }

    public CheckList(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String title,
            Boolean isCompleted) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public CheckList(String title, Boolean isCompleted, User user, TodoCard todoCard) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.user = user;
        this.todoCard = todoCard;
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

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public List<CheckListItem> getCheckListItems() {
        return checkListItems;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTodoCard(TodoCard todoCard) {
        this.todoCard = todoCard;
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

    public void setCheckListItems(List<CheckListItem> checkListItems) {
        this.checkListItems = checkListItems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((isCompleted == null) ? 0 : isCompleted.hashCode());
        result = prime * result + ((todoCard == null) ? 0 : todoCard.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((checkListItems == null) ? 0 : checkListItems.hashCode());
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
        CheckList other = (CheckList) obj;
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
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (isCompleted == null) {
            if (other.isCompleted != null)
                return false;
        } else if (!isCompleted.equals(other.isCompleted))
            return false;
        if (todoCard == null) {
            if (other.todoCard != null)
                return false;
        } else if (!todoCard.equals(other.todoCard))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (checkListItems == null) {
            if (other.checkListItems != null)
                return false;
        } else if (!checkListItems.equals(other.checkListItems))
            return false;
        return true;
    }


}
