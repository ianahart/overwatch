package com.hart.overwatch.profile.dto;

import java.util.List;

public class SkillsDto {

    private List<ItemDto> languages;

    private List<ItemDto> programmingLanguages;

    private List<ItemDto> qualifications;


    public SkillsDto() {

    }

    public SkillsDto(List<ItemDto> languages, List<ItemDto> programmingLanguages,
            List<ItemDto> qualifications) {
        this.languages = languages;
        this.programmingLanguages = programmingLanguages;
        this.qualifications = qualifications;
    }

    public List<ItemDto> getLanguages() {
        return languages;
    }

    public List<ItemDto> getQualifications() {
        return qualifications;
    }

    public List<ItemDto> getProgrammingLanguages() {
        return programmingLanguages;
    }

    public void setLanguages(List<ItemDto> languages) {
        this.languages = languages;
    }

    public void setQualifications(List<ItemDto> qualifications) {
        this.qualifications = qualifications;
    }

    public void setProgrammingLanguages(List<ItemDto> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }
}
