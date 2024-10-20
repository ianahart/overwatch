package com.hart.overwatch.profile.response;

import com.hart.overwatch.profile.dto.PackagesDto;

public class GetProfilePackageResponse {

    private String message;

    private PackagesDto data;

    public GetProfilePackageResponse() {

    }

    public GetProfilePackageResponse(String message, PackagesDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PackagesDto getData() {
        return data;
    }

    public void setData(PackagesDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
