package com.hart.overwatch.customfield.request;

import java.util.List;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionPayloadDto;
import jakarta.validation.constraints.Size;

public class CreateCustomFieldRequest {

    private Long todoCardId;

    private Long userId;

    @Size(max = 50, message = "Field type must be under 50 characters")
    private String fieldType;

    @Size(max = 50, message = "Field name must be under 50 characters")
    private String fieldName;

    @Size(max = 50, message = "Value must be under 50 characters")
    private String selectedValue;

    private List<DropDownOptionPayloadDto> dropDownOptions;

    public CreateCustomFieldRequest() {

    }

    public CreateCustomFieldRequest(Long todoCardId, Long userId, String fieldType,
            String fieldName, String selectedValue,
            List<DropDownOptionPayloadDto> dropDownOptions) {
        this.todoCardId = todoCardId;
        this.userId = userId;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.selectedValue = selectedValue;
        this.dropDownOptions = dropDownOptions;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTodoCardId() {
        return todoCardId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public List<DropDownOptionPayloadDto> getDropDownOptions() {
        return dropDownOptions;
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

    public void setDropDownOptions(List<DropDownOptionPayloadDto> dropDownOptions) {
        this.dropDownOptions = dropDownOptions;
    }
}
