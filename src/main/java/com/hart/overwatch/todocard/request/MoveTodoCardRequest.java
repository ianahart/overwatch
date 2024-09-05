package com.hart.overwatch.todocard.request;

public class MoveTodoCardRequest {

    private Long sourceListId;

    private Long destinationListId;

    private Integer newIndex;


    public MoveTodoCardRequest() {

    }

    public MoveTodoCardRequest(Long sourceListId, Long destinationListId, Integer newIndex) {
        this.sourceListId = sourceListId;
        this.destinationListId = destinationListId;
        this.newIndex = newIndex;
    }

    public Integer getNewIndex() {
        return newIndex;
    }

    public Long getSourceListId() {
        return sourceListId;
    }

    public Long getDestinationListId() {
        return destinationListId;
    }

    public void setNewIndex(Integer newIndex) {
        this.newIndex = newIndex;
    }

    public void setSourceListId(Long sourceListId) {
        this.sourceListId = sourceListId;
    }

    public void setDestinationListId(Long destinationListId) {
        this.destinationListId = destinationListId;
    }
}
