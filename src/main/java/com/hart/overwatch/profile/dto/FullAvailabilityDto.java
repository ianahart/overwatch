package com.hart.overwatch.profile.dto;

import java.util.List;
import java.util.Objects;

public class FullAvailabilityDto {

    private String day;
    private List<AvailabilityDto> slots;

    public FullAvailabilityDto() {}

    public FullAvailabilityDto(String day, List<AvailabilityDto> slots) {
        this.day = day;
        this.slots = slots;
    }

    public String getDay() {
        return day;
    }

    public List<AvailabilityDto> getSlots() {
        return slots;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setSlots(List<AvailabilityDto> slots) {
        this.slots = slots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FullAvailabilityDto that = (FullAvailabilityDto) o;
        return Objects.equals(day, that.day) && Objects.equals(slots, that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, slots);
    }
}
