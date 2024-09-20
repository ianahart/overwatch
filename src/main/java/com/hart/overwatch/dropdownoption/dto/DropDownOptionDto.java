package com.hart.overwatch.dropdownoption.dto;

public class DropDownOptionDto {

    private Long id;

    private Long customFieldId;

    private String optionValue;

    public DropDownOptionDto() {

    }

    public DropDownOptionDto(Long id, Long customFieldId, String optionValue) {
        this.id = id;
        this.customFieldId = customFieldId;
        this.optionValue = optionValue;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomFieldId() {
        return customFieldId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public void setCustomFieldId(Long customFieldId) {
        this.customFieldId = customFieldId;
    }
}
