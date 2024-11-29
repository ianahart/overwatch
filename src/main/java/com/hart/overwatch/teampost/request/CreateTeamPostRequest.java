package com.hart.overwatch.teampost.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTeamPostRequest {

    @NotNull(message = "Missing required userId")
    private Long userId;

    @Size(min = 1, max = 600, message = "Code must bet between 1 and 600 characters")
    private String code;

    @Size(min = 1, max = 100, message = "Language must be between 1 and 100 characters")
    private String language;

    public CreateTeamPostRequest() {

    }

    public CreateTeamPostRequest(Long userId, String code, String language) {
        this.userId = userId;
        this.code = code;
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
