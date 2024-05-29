package com.hart.overwatch.profile;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.profile.dto.AdditionalInfoDto;
import com.hart.overwatch.profile.dto.BasicInfoDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.PackageDto;
import com.hart.overwatch.profile.dto.PackagesDto;
import com.hart.overwatch.profile.dto.ProfileDto;
import com.hart.overwatch.profile.dto.ProfileSetupDto;
import com.hart.overwatch.profile.dto.SkillsDto;
import com.hart.overwatch.profile.dto.WorkExpDto;
import com.hart.overwatch.profile.dto.WorkExpsDto;
import com.hart.overwatch.profile.request.RemoveAvatarRequest;
import com.hart.overwatch.profile.request.UpdateProfileRequest;
import com.hart.overwatch.profile.request.UploadAvatarRequest;
import com.hart.overwatch.user.UserService;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final AmazonService amazonService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserService userService,
            AmazonService amazonService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.amazonService = amazonService;
    }

    public Profile getProfileById(Long profileId) {

        return this.profileRepository.findById(profileId).orElseThrow(() -> new NotFoundException(
                String.format("A profile with the id %d was not found", profileId)));
    }

    public Profile createProfile() {
        Profile profile = new Profile();

        this.profileRepository.save(profile);

        return profile;
    }


    public Profile checkOwnerShip(Long profileId) {
        if (profileId == null) {
            throw new BadRequestException("Profile Id is missing");
        }
        Profile profile = getProfileById(profileId);
        Long currentUserProfileId =
                this.userService.getCurrentlyLoggedInUser().getProfile().getId();

        if (profile.getId() != currentUserProfileId) {
            throw new ForbiddenException("Cannot update another user's profile");
        }

        return profile;
    }


    public AvatarDto uploadAvatar(UploadAvatarRequest request, Long profileId) {
        Profile profile = checkOwnerShip(profileId);

        if (profile.getAvatarFilename() != null) {
            this.amazonService.deleteBucketObject("arrow-date", profile.getAvatarFilename());
            profile.setAvatarFilename(null);
            profile.setAvatarUrl(null);
        }

        HashMap<String, String> result = this.amazonService.putS3Object("arrow-date",
                request.getAvatar().getOriginalFilename(), request.getAvatar());

        profile.setAvatarFilename(result.get("filename"));
        profile.setAvatarUrl(result.get("objectUrl"));

        this.profileRepository.save(profile);

        return new AvatarDto(profile.getAvatarUrl());
    }

    public void removeAvatar(Long profileId, RemoveAvatarRequest request) {
        Profile profile = checkOwnerShip(profileId);

        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setAvatarFilename(request.getAvatarFilename());

        this.profileRepository.save(profile);
    }


    private List<WorkExpDto> cleanWorkExp(List<WorkExpDto> workExps) {
        return workExps.stream()
                .map(v -> new WorkExpDto(v.getId(), Jsoup.clean(v.getTitle(), Safelist.none()),
                        Jsoup.clean(v.getDesc(), Safelist.none())))
                .collect(Collectors.toList());
    }

    private FullPackageDto cleanPckg(FullPackageDto pckg) {

        List<PackageDto> items = pckg
                .getItems().stream().map(v -> (new PackageDto(v.getId(),
                        Jsoup.clean(v.getName(), Safelist.none()), v.getIsEditing())))
                .collect(Collectors.toList());

        return new FullPackageDto(Jsoup.clean(pckg.getPrice(), Safelist.none()),
                Jsoup.clean(pckg.getDescription(), Safelist.none()), items);
    }

    private List<ItemDto> cleanSkills(List<ItemDto> skills) {
        return skills.stream().map(v -> (new ItemDto(v.getId(), v.getName())))
                .collect(Collectors.toList());
    }

    public void updateProfile(Long profileId, UpdateProfileRequest request) {
        Profile profile = checkOwnerShip(profileId);

        profile.setWorkExp(cleanWorkExp(request.getWorkExps()));
        profile.setContactNumber(Jsoup.clean(request.getContactNumber(), Safelist.none()));
        profile.setEmail(Jsoup.clean(request.getEmail(), Safelist.none()));
        profile.setFullName(Jsoup.clean(request.getFullName(), Safelist.none()));
        profile.setUserName(Jsoup.clean(request.getUserName(), Safelist.none()));
        profile.setBio(Jsoup.clean(request.getBio(), Safelist.none()));
        profile.setTagLine(Jsoup.clean(request.getTagLine(), Safelist.none()));
        profile.setLanguages(cleanSkills(request.getLanguages()));
        profile.setProgrammingLanguages(cleanSkills(request.getProgrammingLanguages()));
        profile.setQualifications(cleanSkills(request.getQualifications()));
        profile.setBasic(cleanPckg(request.getBasic()));
        profile.setStandard(cleanPckg(request.getStandard()));
        profile.setPro(cleanPckg(request.getPro()));
        profile.setAvailability(request.getAvailability());
        profile.setMoreInfo(request.getMoreInfo());

        this.profileRepository.save(profile);

    }

    public ProfileDto getProfile(Long profileId) {
        Profile profile = getProfileById(profileId);

        BasicInfoDto basicInfo = new BasicInfoDto(profile.getFullName(), profile.getUserName(),
                profile.getEmail(), profile.getContactNumber());

        ProfileSetupDto profileSetup =
                new ProfileSetupDto(profile.getAvatarUrl(), profile.getTagLine(), profile.getBio());

        SkillsDto skills = new SkillsDto(profile.getLanguages(), profile.getProgrammingLanguages(),
                profile.getQualifications());

        PackagesDto packages =
                new PackagesDto(profile.getBasic(), profile.getStandard(), profile.getPro());



        WorkExpsDto workExps = new WorkExpsDto(profile.getWorkExp());


        AdditionalInfoDto additionalInfo =
                new AdditionalInfoDto(profile.getAvailability(), profile.getMoreInfo());

        return new ProfileDto(basicInfo, profileSetup, skills, workExps, packages, additionalInfo);

    }



}
