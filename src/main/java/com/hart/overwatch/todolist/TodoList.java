package com.hart.overwatch.todolist;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
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

@Entity()
@Table(name = "todo_list")
public class TodoList {

    @Id
    @SequenceGenerator(name = "todo_list_sequence", sequenceName = "todo_list_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_list_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "index")
    private Integer index;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private WorkSpace workSpace;

    @OneToMany(mappedBy = "todoList", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = false)
    private List<TodoCard> todoCards;


    public TodoList() {

    }

    public TodoList(Long id, Timestamp createdAt, Timestamp updatedAt, String title,
            Integer index) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.index = index;
    }

    public TodoList(User user, WorkSpace workSpace, String title, Integer index) {
        this.user = user;
        this.workSpace = workSpace;
        this.title = title;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIndex() {
        return index;
    }

    public List<TodoCard> getTodoCards() {
        return todoCards;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public WorkSpace getWorkSpace() {
        return workSpace;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTodoCards(List<TodoCard> todoCards) {
        this.todoCards = todoCards;
    }
}
