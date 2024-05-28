package com.hart.overwatch.profile;

import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hart.overwatch.profile.dto.FullAvailabilityDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.WorkExpDto;
import com.hart.overwatch.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.Type;



@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @SequenceGenerator(name = "profile_sequence", sequenceName = "profile_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "avatar_filename", length = 400)
    private String avatarFilename;

    @Column(name = "avatar_url", length = 400)
    private String avatarUrl;

    @Type(JsonType.class)
    @Column(name = "work_exp", columnDefinition = "jsonb")
    private List<WorkExpDto> workExp;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "bio", length = 400)
    private String bio;

    @Column(name = "tag_line", length = 50)
    private String tagLine;

    @Type(JsonType.class)
    @Column(name = "languages", columnDefinition = "jsonb")
    private List<ItemDto> languages;

    @Type(JsonType.class)
    @Column(name = "programming_languages", columnDefinition = "jsonb")
    private List<ItemDto> programmingLanguages;

    @Type(JsonType.class)
    @Column(name = "qualifications", columnDefinition = "jsonb")
    private List<ItemDto> qualifications;

    @Type(JsonType.class)
    @Column(name = "basic", columnDefinition = "jsonb")
    private FullPackageDto basic;

    @Type(JsonType.class)
    @Column(name = "standard", columnDefinition = "jsonb")
    private FullPackageDto standard;

    @Type(JsonType.class)
    @Column(name = "pro", columnDefinition = "jsonb")
    private FullPackageDto pro;

    @Type(JsonType.class)
    @Column(name = "availability", columnDefinition = "jsonb")
    private List<FullAvailabilityDto> availability;

    @Column(name = "more_info", length = 300)
    private String moreInfo;



    @JsonIgnore
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;

    public Profile() {

    }

    public Profile(Long id, Timestamp createdAt, Timestamp updatedAt, String avatarFilename,
            String avatarUrl, String schoolName, List<WorkExpDto> workExp, String contactNumber,
            String email, String fullName, String userName, String bio, String tagLine,
            List<ItemDto> languages, List<ItemDto> programmingLanguages,
            List<ItemDto> qualifications, FullPackageDto basic, FullPackageDto standard,
            FullPackageDto pro, List<FullAvailabilityDto> availability, String moreInfo) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.avatarFilename = avatarFilename;
        this.avatarUrl = avatarUrl;
        this.workExp = workExp;
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

    public Long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAvatarFilename(String avatarFilename) {
        this.avatarFilename = avatarFilename;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public FullPackageDto getPro() {
        return pro;
    }

    public List<ItemDto> getLanguages() {
        return languages;
    }

    public FullPackageDto getStandard() {
        return standard;
    }

    public List<ItemDto> getQualifications() {
        return qualifications;
    }

    public FullPackageDto getBasic() {
        return basic;
    }

    public List<ItemDto> getProgrammingLanguages() {
        return programmingLanguages;
    }

    public List<WorkExpDto> getWorkExp() {
        return workExp;
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


    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public void setBasic(FullPackageDto basic) {
        this.basic = basic;
    }

    public void setStandard(FullPackageDto standard) {
        this.standard = standard;
    }

    public void setWorkExp(List<WorkExpDto> workExp) {
        this.workExp = workExp;
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


}

