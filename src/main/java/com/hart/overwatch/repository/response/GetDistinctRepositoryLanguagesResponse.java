package com.hart.overwatch.repository.response;

import java.util.List;

public class GetDistinctRepositoryLanguagesResponse {

    private String message;

    private List<String> data;



    public GetDistinctRepositoryLanguagesResponse() {

    }

    public GetDistinctRepositoryLanguagesResponse(String message, List<String> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
