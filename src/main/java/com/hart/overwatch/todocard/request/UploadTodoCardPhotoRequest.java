package com.hart.overwatch.todocard.request;

import org.springframework.web.multipart.MultipartFile;

public class UploadTodoCardPhotoRequest {

    private MultipartFile file;

    public UploadTodoCardPhotoRequest() {

    }

    public UploadTodoCardPhotoRequest(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
