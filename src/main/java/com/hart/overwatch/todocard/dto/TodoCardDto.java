package com.hart.overwatch.todocard.dto;

import java.time.LocalDateTime;

public class TodoCardDto {
    private Long id;

    private Long todoListId;

    private Long userId;

    private LocalDateTime createdAt;

    private String label;

    private String title;

    private String color;

    private Integer index;

    private String details;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String photo;

    public TodoCardDto() {

    }

    public TodoCardDto(Long id, Long todoListId, Long userId, LocalDateTime createdAt, String label,
            String title, String color, Integer index, String details, LocalDateTime startDate,
            LocalDateTime endDate, String photo) {

        this.id = id;
        this.todoListId = todoListId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.label = label;
        this.title = title;
        this.color = color;
        this.index = index;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Integer getIndex() {
        return index;
    }

    public Long getTodoListId() {
        return todoListId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTodoListId(Long todoListId) {
        this.todoListId = todoListId;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
