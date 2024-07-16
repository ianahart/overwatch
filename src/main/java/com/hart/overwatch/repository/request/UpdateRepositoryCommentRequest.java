package com.hart.overwatch.repository.request;


public class UpdateRepositoryCommentRequest {

    private String comment;


    public UpdateRepositoryCommentRequest() {

    }


    public UpdateRepositoryCommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
