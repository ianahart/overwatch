package com.hart.overwatch.todocard.request;

public class ReorderTodoCardRequest {

    private Long todoListId;

    private Integer newIndex;

    private Integer oldIndex;

    public ReorderTodoCardRequest() {

    }

    public ReorderTodoCardRequest(Long todoListId, Integer newIndex, Integer oldIndex) {
          this.todoListId = todoListId;
        this.newIndex = newIndex;
        this.oldIndex = oldIndex;
    }

    public Long getTodoListId() {
        return todoListId;
    }

    public Integer getNewIndex() {
        return newIndex;
    }

    public Integer getOldIndex() {
        return oldIndex;
    }

    public void setNewIndex(Integer newIndex) {
        this.newIndex = newIndex;
    }

    public void setOldIndex(Integer oldIndex) {
        this.oldIndex = oldIndex;
    }

    public void setTodoListId(Long todoListId) {
        this.todoListId = todoListId;
    }
}
