package com.hart.overwatch.profile.dto;

import java.util.List;

public class FullPackageDto {

    private String description;

    private List<PackageDto> items;


    public FullPackageDto() {

    }

    public FullPackageDto(String description, List<PackageDto> items) {
        this.description = description;
        this.items = items;
    }

    public List<PackageDto> getItems() {
        return items;
    }

    public String getDescription() {
        return description;
    }

    public void setItems(List<PackageDto> items) {
        this.items = items;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
