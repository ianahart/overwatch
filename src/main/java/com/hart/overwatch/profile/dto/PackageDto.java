package com.hart.overwatch.profile.dto;

public class PackageDto {

    private String id;

    private String name;

    private Integer isEditing;


    public PackageDto() {

    }

    public PackageDto(String id, String name, Integer isEditing) {
        this.id = id;
        this.name = name;
        this.isEditing = isEditing;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getIsEditing() {
        return isEditing;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsEditing(Integer isEditing) {
        this.isEditing = isEditing;
    }

}
