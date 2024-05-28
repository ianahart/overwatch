package com.hart.overwatch.profile.dto;

import java.util.List;

public class AdditionalInfoDto {

    private List<FullAvailabilityDto> availability;

    private String moreInfo;


    public AdditionalInfoDto() {

    }


    public AdditionalInfoDto(List<FullAvailabilityDto> availability, String moreInfo) {
        this.availability = availability;
        this.moreInfo = moreInfo;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public List<FullAvailabilityDto> getAvailability() {
        return availability;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public void setAvailability(List<FullAvailabilityDto> availability) {
        this.availability = availability;
    }

}
