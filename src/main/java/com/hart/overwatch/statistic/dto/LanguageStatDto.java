package com.hart.overwatch.statistic.dto;

public class LanguageStatDto {

    private String language;

    private Integer count;

    public LanguageStatDto() {

    }

    public LanguageStatDto(String language, Integer count) {
        this.language = language;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public String getLanguage() {
        return language;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
