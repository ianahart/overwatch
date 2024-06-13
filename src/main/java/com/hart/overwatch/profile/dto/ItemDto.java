package com.hart.overwatch.profile.dto;

import java.util.Objects;

public class ItemDto {

    private String id;
    private String name;
    private Boolean isCompatible;

    public ItemDto() {}

    public ItemDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemDto(String id, String name, Boolean isCompatible) {
        this.id = id;
        this.name = name;
        this.isCompatible = isCompatible;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsCompatible() {
        return isCompatible;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsCompatible(Boolean isCompatible) {
        this.isCompatible = isCompatible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id) && Objects.equals(name, itemDto.name)
                && Objects.equals(isCompatible, itemDto.isCompatible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isCompatible);
    }
}
