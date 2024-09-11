package com.hart.overwatch.todocard;

import java.util.List;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

@Entity()
@Table(name = "todo_card")
public class TodoCard {

    @Id
    @SequenceGenerator(name = "todo_card_sequence", sequenceName = "todo_card_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_card_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "label", length = 50)
    private String label;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "index")
    private Integer index;

    @Column(name = "details", length = 1000)
    private String details;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "photo", length = 255)
    private String photo;

    @Column(name = "upload_photo_url", length = 255)
    private String uploadPhotoUrl;

    @Column(name = "upload_photo_filename", length = 255)
    private String uploadPhotoFileName;

    @OneToMany(mappedBy = "todoCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ActiveLabel> activeLabels;

    @OneToMany(mappedBy = "todoCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CheckList> checkLists;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "todo_list_id", referencedColumnName = "id")
    private TodoList todoList;

    public TodoCard() {

    }

    public TodoCard(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String label,
            String title, String color, Integer index, String details, LocalDateTime startDate,
            LocalDateTime endDate, String photo, String uploadPhotoUrl, String uploadPhotoFileName

    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.label = label;
        this.title = title;
        this.color = color;
        this.index = index;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photo = photo;
        this.uploadPhotoUrl = uploadPhotoUrl;
        this.uploadPhotoFileName = uploadPhotoFileName;
    }

    public TodoCard(String title, Integer index, User user, TodoList todoList) {
        this.title = title;
        this.index = index;
        this.user = user;
        this.todoList = todoList;
    }

    public Long getId() {
        return id;
    }

    public List<CheckList> getCheckLists() {
        return checkLists;
    }

    public List<ActiveLabel> getActiveLabels() {
        return activeLabels;
    }

    public User getUser() {
        return user;
    }

    public String getColor() {
        return color;
    }

    public String getUploadPhotoUrl() {
        return uploadPhotoUrl;
    }

    public String getUploadPhotoFileName() {
        return uploadPhotoFileName;
    }

    public String getLabel() {
        return label;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Integer getIndex() {
        return index;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
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

    public void setColor(String color) {
        this.color = color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActiveLabels(List<ActiveLabel> activeLabels) {
        this.activeLabels = activeLabels;
    }

    public void setUploadPhotoUrl(String uploadPhotoUrl) {
        this.uploadPhotoUrl = uploadPhotoUrl;
    }

    public void setUploadPhotoFileName(String uploadPhotoFileName) {
        this.uploadPhotoFileName = uploadPhotoFileName;
    }

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }
}
