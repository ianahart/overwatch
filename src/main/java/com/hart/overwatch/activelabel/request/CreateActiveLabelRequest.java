package com.hart.overwatch.activelabel.request;

public class CreateActiveLabelRequest {

    private Long todoCardId;

    private Long labelId;


    public CreateActiveLabelRequest() {

    }

    public CreateActiveLabelRequest(Long todoCardId, Long labelId) {
        this.todoCardId = todoCardId;
        this.labelId = labelId;
    }

    public Long getLabelId() {
        return labelId;
    }

    public Long getTodoCardId() {
        return todoCardId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }
}
