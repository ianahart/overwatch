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
import com.hart.overwatch.activity.Activity;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.customfield.CustomField;
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

    @OneToMany(mappedBy = "todoCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Activity> activities;

    @OneToMany(mappedBy = "todoCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CustomField> customFields;



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

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public List<Activity> getActivities() {
        return activities;
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

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
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

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((photo == null) ? 0 : photo.hashCode());
        result = prime * result + ((uploadPhotoUrl == null) ? 0 : uploadPhotoUrl.hashCode());
        result = prime * result + ((uploadPhotoFileName == null) ? 0 : uploadPhotoFileName.hashCode());
        result = prime * result + ((activeLabels == null) ? 0 : activeLabels.hashCode());
        result = prime * result + ((checkLists == null) ? 0 : checkLists.hashCode());
        result = prime * result + ((activities == null) ? 0 : activities.hashCode());
        result = prime * result + ((customFields == null) ? 0 : customFields.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((todoList == null) ? 0 : todoList.hashCode());
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
        TodoCard other = (TodoCard) obj;
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
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (index == null) {
            if (other.index != null)
                return false;
        } else if (!index.equals(other.index))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (photo == null) {
            if (other.photo != null)
                return false;
        } else if (!photo.equals(other.photo))
            return false;
        if (uploadPhotoUrl == null) {
            if (other.uploadPhotoUrl != null)
                return false;
        } else if (!uploadPhotoUrl.equals(other.uploadPhotoUrl))
            return false;
        if (uploadPhotoFileName == null) {
            if (other.uploadPhotoFileName != null)
                return false;
        } else if (!uploadPhotoFileName.equals(other.uploadPhotoFileName))
            return false;
        if (activeLabels == null) {
            if (other.activeLabels != null)
                return false;
        } else if (!activeLabels.equals(other.activeLabels))
            return false;
        if (checkLists == null) {
            if (other.checkLists != null)
                return false;
        } else if (!checkLists.equals(other.checkLists))
            return false;
        if (activities == null) {
            if (other.activities != null)
                return false;
        } else if (!activities.equals(other.activities))
            return false;
        if (customFields == null) {
            if (other.customFields != null)
                return false;
        } else if (!customFields.equals(other.customFields))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (todoList == null) {
            if (other.todoList != null)
                return false;
        } else if (!todoList.equals(other.todoList))
            return false;
        return true;
    }


}
