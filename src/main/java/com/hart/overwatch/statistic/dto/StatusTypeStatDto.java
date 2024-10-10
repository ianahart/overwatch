package com.hart.overwatch.statistic.dto;

public class StatusTypeStatDto {

    private String status;

    private Integer count;


    public StatusTypeStatDto() {

    }

    public StatusTypeStatDto(String status, Integer count) {
         this.status = status;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
