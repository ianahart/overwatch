package com.hart.overwatch.profile;

import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hart.overwatch.favorite.Favorite;
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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(name = "tag_line", length = 150)
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

    @Column(name = "is_visible")
    private Boolean isVisible;



    @JsonIgnore
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;


    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Favorite> favorites;


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


    public Profile(Timestamp createdAt, Timestamp updatedAt, String avatarFilename,
            String avatarUrl, String schoolName, List<WorkExpDto> workExp, String contactNumber,
            String email, String fullName, String userName, String bio, String tagLine,
            List<ItemDto> languages, List<ItemDto> programmingLanguages,
            List<ItemDto> qualifications, FullPackageDto basic, FullPackageDto standard,
            FullPackageDto pro, List<FullAvailabilityDto> availability, String moreInfo) {
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

    public Boolean getIsVisible() {
        return isVisible;
    }

    public List<Favorite> getFavorites() {
        return favorites;
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

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((avatarFilename == null) ? 0 : avatarFilename.hashCode());
        result = prime * result + ((avatarUrl == null) ? 0 : avatarUrl.hashCode());
        result = prime * result + ((workExp == null) ? 0 : workExp.hashCode());
        result = prime * result + ((contactNumber == null) ? 0 : contactNumber.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((bio == null) ? 0 : bio.hashCode());
        result = prime * result + ((tagLine == null) ? 0 : tagLine.hashCode());
        result = prime * result + ((languages == null) ? 0 : languages.hashCode());
        result = prime * result + ((programmingLanguages == null) ? 0 : programmingLanguages.hashCode());
        result = prime * result + ((qualifications == null) ? 0 : qualifications.hashCode());
        result = prime * result + ((basic == null) ? 0 : basic.hashCode());
        result = prime * result + ((standard == null) ? 0 : standard.hashCode());
        result = prime * result + ((pro == null) ? 0 : pro.hashCode());
        result = prime * result + ((availability == null) ? 0 : availability.hashCode());
        result = prime * result + ((moreInfo == null) ? 0 : moreInfo.hashCode());
        result = prime * result + ((isVisible == null) ? 0 : isVisible.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((favorites == null) ? 0 : favorites.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Profile other = (Profile) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        if (avatarFilename == null) {
            if (other.avatarFilename != null)
                return false;
        } else if (!avatarFilename.equals(other.avatarFilename))
            return false;
        if (avatarUrl == null) {
            if (other.avatarUrl != null)
                return false;
        } else if (!avatarUrl.equals(other.avatarUrl))
            return false;
        if (workExp == null) {
            if (other.workExp != null)
                return false;
        } else if (!workExp.equals(other.workExp))
            return false;
        if (contactNumber == null) {
            if (other.contactNumber != null)
                return false;
        } else if (!contactNumber.equals(other.contactNumber))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (bio == null) {
            if (other.bio != null)
                return false;
        } else if (!bio.equals(other.bio))
            return false;
        if (tagLine == null) {
            if (other.tagLine != null)
                return false;
        } else if (!tagLine.equals(other.tagLine))
            return false;
        if (languages == null) {
            if (other.languages != null)
                return false;
        } else if (!languages.equals(other.languages))
            return false;
        if (programmingLanguages == null) {
            if (other.programmingLanguages != null)
                return false;
        } else if (!programmingLanguages.equals(other.programmingLanguages))
            return false;
        if (qualifications == null) {
            if (other.qualifications != null)
                return false;
        } else if (!qualifications.equals(other.qualifications))
            return false;
        if (basic == null) {
            if (other.basic != null)
                return false;
        } else if (!basic.equals(other.basic))
            return false;
        if (standard == null) {
            if (other.standard != null)
                return false;
        } else if (!standard.equals(other.standard))
            return false;
        if (pro == null) {
            if (other.pro != null)
                return false;
        } else if (!pro.equals(other.pro))
            return false;
        if (availability == null) {
            if (other.availability != null)
                return false;
        } else if (!availability.equals(other.availability))
            return false;
        if (moreInfo == null) {
            if (other.moreInfo != null)
                return false;
        } else if (!moreInfo.equals(other.moreInfo))
            return false;
        if (isVisible == null) {
            if (other.isVisible != null)
                return false;
        } else if (!isVisible.equals(other.isVisible))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (favorites == null) {
            if (other.favorites != null)
                return false;
        } else if (!favorites.equals(other.favorites))
            return false;
        return true;
    }


}
