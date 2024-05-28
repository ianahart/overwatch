package com.hart.overwatch.profile.dto;

import java.util.Objects;

public class ItemDto {

    private String id;
    private String name;

    public ItemDto() {}

    public ItemDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id) && Objects.equals(name, itemDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
