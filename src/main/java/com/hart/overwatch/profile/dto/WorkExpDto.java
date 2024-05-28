
package com.hart.overwatch.profile.dto;

import java.util.Objects;

public class WorkExpDto {

    private String id;
    private String title;
    private String desc;

    public WorkExpDto() {}

    public WorkExpDto(String id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkExpDto that = (WorkExpDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title)
                && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, desc);
    }
}
