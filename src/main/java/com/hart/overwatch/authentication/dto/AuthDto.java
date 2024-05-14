package com.hart.overwatch.authentication.dto;

import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.user.dto.UserDto;

public class AuthDto {

    private UserDto user;

    private String jwtToken;

    private RefreshToken refreshToken;


    public AuthDto() {

    }

    public AuthDto(UserDto user, String jwtToken, RefreshToken refreshToken) {
        this.user = user;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    public UserDto getUser() {
        return user;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

}
