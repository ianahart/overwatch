package com.hart.overwatch.profile.request;

import java.util.List;
import com.hart.overwatch.profile.dto.FullAvailabilityDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.WorkExpDto;

public class UpdateProfileRequest {

    private List<WorkExpDto> workExps;

    private String contactNumber;

    private String email;

    private String fullName;

    private String userName;

    private String bio;

    private String tagLine;

    private List<ItemDto> languages;

    private List<ItemDto> programmingLanguages;

    private List<ItemDto> qualifications;

    private FullPackageDto basic;

    private FullPackageDto standard;

    private FullPackageDto pro;

    private List<FullAvailabilityDto> availability;

    private String moreInfo;


    public UpdateProfileRequest() {

    }

    public UpdateProfileRequest(List<WorkExpDto> workExps, String contactNumber, String email,
            String fullName, String userName, String bio, String tagLine, List<ItemDto> languages,
            List<ItemDto> programmingLanguages, List<ItemDto> qualifications, FullPackageDto basic,
            FullPackageDto standard, FullPackageDto pro, List<FullAvailabilityDto> availability,
            String moreInfo) {
        this.workExps = workExps;
        this.contactNumber = contactNumber;
        this.email = email;
        this.fullName = fullName;
        this.userName = userName;
        this.bio = bio;
        this.tagLine = tagLine;
        this.languages = languages;
        this.programmingLanguages = programmingLanguages;
        this.qualifications = qualifications;
        this.basic = basic;
        this.standard = standard;
        this.pro = pro;
        this.availability = availability;
        this.moreInfo = moreInfo;
    }



    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getUserName() {
        return userName;
    }

    public FullPackageDto getPro() {
        return pro;
    }

    public String getContactNumber() {
        return contactNumber;
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

    public FullPackageDto getBasic() {
        return basic;
    }

    public FullPackageDto getStandard() {
        return standard;
    }


    public List<WorkExpDto> getWorkExps() {
        return workExps;
    }

    public List<FullAvailabilityDto> getAvailability() {
        return availability;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPro(FullPackageDto pro) {
        this.pro = pro;
    }

    public void setBasic(FullPackageDto basic) {
        this.basic = basic;
    }

    public void setStandard(FullPackageDto standard) {
        this.standard = standard;
    }


    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLanguages(List<ItemDto> languages) {
        this.languages = languages;
    }

    public void setQualifications(List<ItemDto> qualifications) {
        this.qualifications = qualifications;
    }

    public void setAvailability(List<FullAvailabilityDto> availability) {
        this.availability = availability;
    }

    public void setProgrammingLanguages(List<ItemDto> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    public void setWorkExps(List<WorkExpDto> workExps) {
        this.workExps = workExps;
    }
}
