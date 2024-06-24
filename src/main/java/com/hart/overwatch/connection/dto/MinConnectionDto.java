package com.hart.overwatch.connection.dto;

import com.hart.overwatch.connection.RequestStatus;

public class MinConnectionDto {

    private Long id;

    private RequestStatus status;


    public MinConnectionDto() {

    }

    public MinConnectionDto(Long id, RequestStatus status) {
        this.id = id;
        this.status = status;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }



}


