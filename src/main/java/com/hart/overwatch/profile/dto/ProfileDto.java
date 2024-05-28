package com.hart.overwatch.profile.dto;

public class ProfileDto {

    private BasicInfoDto basicInfo;

    private ProfileSetupDto profileSetup;

    private SkillsDto skills;

    private WorkExpsDto workExp;

    private PackagesDto pckg;

    private AdditionalInfoDto additionalInfo;


    public ProfileDto() {

    }

    public ProfileDto(BasicInfoDto basicInfo, ProfileSetupDto profileSetup, SkillsDto skills,
            WorkExpsDto workExp, PackagesDto pckg, AdditionalInfoDto additionalInfo) {
        this.basicInfo = basicInfo;
        this.profileSetup = profileSetup;
        this.skills = skills;
        this.workExp = workExp;
        this.pckg = pckg;
        this.additionalInfo = additionalInfo;
    }

    public PackagesDto getPckg() {
        return pckg;
    }

    public SkillsDto getSkills() {
        return skills;
    }

    public WorkExpsDto getWorkExp() {
        return workExp;
    }

    public BasicInfoDto getBasicInfo() {
        return basicInfo;
    }

    public ProfileSetupDto getProfileSetup() {
        return profileSetup;
    }

    public AdditionalInfoDto getAdditionalInfo() {
        return additionalInfo;
    }

    public void setPckg(PackagesDto pckg) {
        this.pckg = pckg;
    }

    public void setSkills(SkillsDto skills) {
        this.skills = skills;
    }

    public void setWorkExp(WorkExpsDto workExp) {
        this.workExp = workExp;
    }

    public void setBasicInfo(BasicInfoDto basicInfo) {
        this.basicInfo = basicInfo;
    }

    public void setProfileSetup(ProfileSetupDto profileSetup) {
        this.profileSetup = profileSetup;
    }

    public void setAdditionalInfo(AdditionalInfoDto additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
