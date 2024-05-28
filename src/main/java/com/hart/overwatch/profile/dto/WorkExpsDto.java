package com.hart.overwatch.profile.dto;

import java.util.List;

public class WorkExpsDto {

    private List<WorkExpDto> workExps;


    public WorkExpsDto() {

    }

    public WorkExpsDto(List<WorkExpDto> workExps) {
        this.workExps = workExps;
    }

    public List<WorkExpDto> getWorkExps() {
        return workExps;
    }

    public void setWorkExps(List<WorkExpDto> workExps) {
        this.workExps = workExps;
    }
}
