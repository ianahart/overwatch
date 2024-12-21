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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = prime * result + ((selectedValue == null) ? 0 : selectedValue.hashCode());
        result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((todoCard == null) ? 0 : todoCard.hashCode());
        result = prime * result + ((dropDownOptions == null) ? 0 : dropDownOptions.hashCode());
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
        CustomField other = (CustomField) obj;
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
        if (fieldName == null) {
            if (other.fieldName != null)
                return false;
        } else if (!fieldName.equals(other.fieldName))
            return false;
        if (selectedValue == null) {
            if (other.selectedValue != null)
                return false;
        } else if (!selectedValue.equals(other.selectedValue))
            return false;
        if (fieldType == null) {
            if (other.fieldType != null)
                return false;
        } else if (!fieldType.equals(other.fieldType))
            return false;
        if (isActive == null) {
            if (other.isActive != null)
                return false;
        } else if (!isActive.equals(other.isActive))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (todoCard == null) {
            if (other.todoCard != null)
                return false;
        } else if (!todoCard.equals(other.todoCard))
            return false;
        if (dropDownOptions == null) {
            if (other.dropDownOptions != null)
                return false;
        } else if (!dropDownOptions.equals(other.dropDownOptions))
            return false;
        return true;
    }


}
