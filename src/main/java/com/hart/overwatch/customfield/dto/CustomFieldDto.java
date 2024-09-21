package com.hart.overwatch.customfield.dto;

import java.util.List;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionDto;

public class CustomFieldDto {

    private Long id;

    private Long userId;

    private Long todoCardId;

    private String fieldType;

    private String fieldName;

    private String selectedValue;

    private Boolean isActive;

    private List<DropDownOptionDto> dropDownOptions;


    public CustomFieldDto() {

    }

    public CustomFieldDto(Long id, Long userId, Long todoCardId, String fieldType, String fieldName,
            String selectedValue, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.todoCardId = todoCardId;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.selectedValue = selectedValue;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTodoCardId() {
        return todoCardId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public List<DropDownOptionDto> getDropDownOptions() {
        return dropDownOptions;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void setDropDownOptions(List<DropDownOptionDto> dropDownOptions) {
        this.dropDownOptions = dropDownOptions;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}


