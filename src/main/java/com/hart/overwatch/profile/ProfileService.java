package com.hart.overwatch.profile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.favorite.FavoriteService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.profile.dto.AdditionalInfoDto;
import com.hart.overwatch.profile.dto.AllProfileDto;
import com.hart.overwatch.profile.dto.BasicInfoDto;
import com.hart.overwatch.profile.dto.FullAvailabilityDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.FullProfileDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.PackageDto;
import com.hart.overwatch.profile.dto.PackagesDto;
import com.hart.overwatch.profile.dto.ProfileDto;
import com.hart.overwatch.profile.dto.ProfileSetupDto;
import com.hart.overwatch.profile.dto.SkillsDto;
import com.hart.overwatch.profile.dto.UserProfileDto;
import com.hart.overwatch.profile.dto.WorkExpDto;
import com.hart.overwatch.profile.dto.WorkExpsDto;
import com.hart.overwatch.profile.request.RemoveAvatarRequest;
import com.hart.overwatch.profile.request.UpdateProfileRequest;
import com.hart.overwatch.profile.request.UploadAvatarRequest;
import com.hart.overwatch.review.ReviewService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.util.MyUtil;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final AmazonService amazonService;
    private final PaginationService paginationService;
    private final ObjectMapper objectMapper;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserService userService,
            AmazonService amazonService, PaginationService paginationService,
            ObjectMapper objectMapper, ReviewService reviewService,
            @Lazy FavoriteService favoriteService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.amazonService = amazonService;
        this.paginationService = paginationService;
        this.objectMapper = objectMapper;
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
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

    public ProfileDto populateProfile(Long profileId) {
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


    public FullProfileDto getProfile(Long profileId) {
        Profile profile = getProfileById(profileId);
        String country = profile.getUser().getLocation() == null ? ""
                : profile.getUser().getLocation().getCountry();
        String city = profile.getUser().getLocation() == null ? ""
                : profile.getUser().getLocation().getCity();

        UserProfileDto userProfile = new UserProfileDto(profile.getId(), profile.getUser().getId(),

                profile.getUser().getRole(), country, profile.getUser().getAbbreviation(), city);

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

        return new FullProfileDto(userProfile, basicInfo, profileSetup, skills, workExps, packages,
                additionalInfo);

    }

    private Page<AllProfileDto> getMostRecent(Pageable pageable) {
        Page<Map<String, Object>> rawResults = profileRepository.getMostRecent(pageable);
        List<AllProfileDto> profiles =
                rawResults.stream().map(this::mapToAllProfileDto).collect(Collectors.toList());
        return new PageImpl<>(profiles, pageable, rawResults.getTotalElements());
    }


    private List<String> getUserProgrammingLanguages() {
        User currentUser = this.userService.getCurrentlyLoggedInUser();
        if (currentUser.getProfile().getProgrammingLanguages() == null) {
            List<String> empty = new ArrayList<String>();
            return empty;
        }
        return currentUser.getProfile().getProgrammingLanguages().stream()
                .map(lang -> lang.getName().toLowerCase()).collect(Collectors.toList());

    }

    private Page<AllProfileDto> getMostRelevant(Pageable pageable) {
        Page<Map<String, Object>> rawResults =
                profileRepository.getMostRelevant(pageable, getUserProgrammingLanguages());

        List<AllProfileDto> profiles =
                rawResults.stream().map(this::mapToAllProfileDto).collect(Collectors.toList());
        return new PageImpl<>(profiles, pageable, rawResults.getTotalElements());
    }


    private Page<AllProfileDto> getDomestic(Pageable pageable) {
        String full = "united states";
        String abbrev = "us";
        Page<Map<String, Object>> rawResults =
                profileRepository.getDomestic(pageable, full, abbrev);
        List<AllProfileDto> profiles =
                rawResults.stream().map(this::mapToAllProfileDto).collect(Collectors.toList());
        return new PageImpl<>(profiles, pageable, rawResults.getTotalElements());
    }


    private void attachProfileStatistics(AllProfileDto profile, Long userId,
            List<FullAvailabilityDto> availability) {
        Boolean weekendsAvailable = availability.stream()
                .filter(obj -> obj.getDay().equalsIgnoreCase("saturday")
                        || obj.getDay().equalsIgnoreCase("sunday"))
                .flatMap(obj -> obj.getSlots().stream())
                .anyMatch(innerObj -> innerObj.getEndTime() != null
                        && innerObj.getStartTime() != null);
        profile.setWeekendsAvailable(weekendsAvailable);

        profile.setNumOfReviews(this.reviewService.getTotalCountOfReviews(userId));

        profile.setReviewAvgRating(this.reviewService.getAvgReviewRating(userId));

        Long currentUserId = this.userService.getCurrentlyLoggedInUser().getId();

        profile.setIsFavorited(this.favoriteService.favoriteExists(currentUserId, profile.getId()));

        profile.setLastActiveReadable(MyUtil.makeReadableDate(profile.getLastActive()));
    }

    private AllProfileDto mapToAllProfileDto(Map<String, Object> rawResult) {
        Long id = ((Number) rawResult.get("id")).longValue();
        Long userId = ((Number) rawResult.get("userId")).longValue();
        String fullName = (String) rawResult.get("fullName");
        String avatarUrl = (String) rawResult.get("avatarUrl");
        String country = (String) rawResult.get("country");
        Timestamp createdAt = (Timestamp) rawResult.get("createdAt");
        String availabilityJson = (String) rawResult.get("availability");
        String programmingLanguagesJson = (String) rawResult.get("programmingLanguages");
        String basicJson = (String) rawResult.get("basic");
        Timestamp lastActiveTimestamp = (Timestamp) rawResult.get("lastActive");
        LocalDateTime lastActive =
                (lastActiveTimestamp != null) ? lastActiveTimestamp.toLocalDateTime() : null;

        List<ItemDto> programmingLanguages = null;
        List<FullAvailabilityDto> availability = null;
        FullPackageDto basic = null;
        try {

            if (basicJson != null) {
                basic = objectMapper.readValue(basicJson, new TypeReference<FullPackageDto>() {});
            }

            if (availabilityJson != null) {
                availability = objectMapper.readValue(availabilityJson,
                        new TypeReference<List<FullAvailabilityDto>>() {});
            }
            if (programmingLanguagesJson != null) {
                programmingLanguages = objectMapper.readValue(programmingLanguagesJson,
                        new TypeReference<List<ItemDto>>() {});
            }
        } catch (Exception e) {
            System.out.println("Unable to parse JSON availability and programming languages: "
                    + e.getMessage());
        }

        List<ItemDto> compatibleProgrammingLanguages =

                getCompatibleProgrammingLanguages(programmingLanguages);

        AllProfileDto allProfile = new AllProfileDto(id, userId, fullName, avatarUrl, country,
                createdAt, availability, compatibleProgrammingLanguages, basic, lastActive);

        if (availability != null) {
            attachProfileStatistics(allProfile, userId, availability);
        }
        return allProfile;
    }

    private List<ItemDto> getCompatibleProgrammingLanguages(List<ItemDto> programmingLanguages) {
        List<String> userProgrammingLanguages = getUserProgrammingLanguages();
        List<ItemDto> compatibleProgrammingLanguages = programmingLanguages.stream().map(v -> {
            if (userProgrammingLanguages.contains(v.getName().toLowerCase())) {
                v.setIsCompatible(true);
            } else {
                v.setIsCompatible(false);
            }
            return v;
        }).toList();

        return compatibleProgrammingLanguages;
    }


    private Page<AllProfileDto> getSaved(Pageable pageable) {
        Long currentUserId = this.userService.getCurrentlyLoggedInUser().getId();
        Page<Long> favorites = this.favoriteService.getFavoriteIds(pageable, currentUserId);
        List<Map<String, Object>> rawResults = profileRepository.getSaved(favorites.getContent());
        List<AllProfileDto> profiles =
                rawResults.stream().map(this::mapToAllProfileDto).collect(Collectors.toList());
        return new PageImpl<>(profiles, pageable, favorites.getTotalElements());
    }



    public PaginationDto<AllProfileDto> getAllProfiles(String filterType, int page, int pageSize,
            String direction) {
        Pageable pageable = filterType.equals("most-recent")
                ? this.paginationService.getSortedPageable(page, pageSize, direction, "desc")
                : this.paginationService.getPageable(page, pageSize, direction);
        Page<AllProfileDto> result = null;
        switch (filterType) {
            case "most-recent":
                result = getMostRecent(pageable);
                break;
            case "domestic":
                result = getDomestic(pageable);
                break;
            case "most-relevant":
                result = getMostRelevant(pageable);
                break;
            case "saved":
                result = getSaved(pageable);
                break;
            default:
                result = getMostRecent(pageable);
                break;
        }


        return new PaginationDto<AllProfileDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }



}
