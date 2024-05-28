package com.hart.overwatch.profile.dto;

import java.util.Objects;

public class AvailabilityDto {

    private String id;
    private String startTime;
    private String endTime;

    public AvailabilityDto() {}

    public AvailabilityDto(String id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AvailabilityDto that = (AvailabilityDto) o;
        return Objects.equals(id, that.id) && Objects.equals(startTime, that.startTime)
                && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime);
    }
}
