package com.hart.overwatch.profile.dto;

import java.util.List;

public class FullPackageDto {

    private String price;

    private String description;

    private List<PackageDto> items;


    public FullPackageDto() {

    }

    public FullPackageDto(String price, String description, List<PackageDto> items) {
        this.price = price;
        this.description = description;
        this.items = items;
    }

    public String getPrice() {
        return price;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
