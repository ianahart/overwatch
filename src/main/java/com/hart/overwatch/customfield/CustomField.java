package com.hart.overwatch.customfield;

import java.util.List;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.dropdownoption.DropDownOption;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.user.User;
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
@Table(name = "custom_field")
public class CustomField {

    @Id
    @SequenceGenerator(name = "custom_field_sequence", sequenceName = "custom_field_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_field_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "field_name", length = 50)
    private String fieldName;

    @Column(name = "selected_value", length = 50, nullable = true)
    private String selectedValue;

    @Column(name = "field_type", length = 50)
    private String fieldType;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "todo_card_id", referencedColumnName = "id")
    private TodoCard todoCard;

    @OneToMany(mappedBy = "customField", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DropDownOption> dropDownOptions;


    public CustomField() {

    }

    public CustomField(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String fieldType,
            String fieldName, String selectedValue, Boolean isActive) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.selectedValue = selectedValue;
        this.isActive = isActive;
    }

    public CustomField(Boolean isActive, String fieldType, String fieldName, String selectedValue,
            User user, TodoCard todoCard) {
        this.isActive = isActive;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.selectedValue = selectedValue;
        this.user = user;
        this.todoCard = todoCard;
    }

    public Long getId() {
        return id;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public List<DropDownOption> getDropDownOptions() {
        return dropDownOptions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public TodoCard getTodoCard() {
        return todoCard;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
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

    public void setDropDownOptions(List<DropDownOption> dropDownOptions) {
        this.dropDownOptions = dropDownOptions;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
