package com.hart.overwatch.authentication.response;

import com.hart.overwatch.user.dto.UserDto;

public class LoginResponse {

    private UserDto user;
    private String token;
    private String refreshToken;
    private Long userId;

    public LoginResponse() {

    }


    public LoginResponse(Long userId) {
        this.userId = userId;
    }

    public LoginResponse(UserDto user, String token, String refreshToken) {
        this.user = user;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public UserDto getUser() {
        return user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}

