package com.hart.overwatch.activelabel.dto;

public class ActiveLabelDto {

    private Long id;

    private Long todoCardId;

    private Long labelId;

    private String color;

    private String title;


    public ActiveLabelDto() {

    }

    public ActiveLabelDto(Long id, Long todoCardId, Long labelId, String color, String title) {
        this.id = id;
        this.todoCardId = todoCardId;
        this.labelId = labelId;
        this.color = color;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public Long getLabelId() {
        return labelId;
    }

    public String getTitle() {
        return title;
    }

    public Long getTodoCardId() {
        return todoCardId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }
}

