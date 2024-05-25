package com.hart.overwatch.profile;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.profile.request.RemoveAvatarRequest;
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
}
