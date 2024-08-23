package com.hart.overwatch.todolist.dto;

import java.sql.Timestamp;

public class TodoListDto {

    private Long id;

    private Long userId;

    private Long workSpaceId;

    private String title;

    private Integer index;

    private Timestamp createdAt;

    public TodoListDto() {

    }

    public TodoListDto(Long id, Long userId, Long workSpaceId, String title, Integer index,
            Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.workSpaceId = workSpaceId;
        this.title = title;
        this.index = index;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
