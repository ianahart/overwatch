package com.hart.overwatch.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.profile.request.RemoveAvatarRequest;
import com.hart.overwatch.profile.request.UpdateProfileRequest;
import com.hart.overwatch.profile.request.UpdateProfileVisibilityRequest;
import com.hart.overwatch.profile.request.UploadAvatarRequest;
import com.hart.overwatch.profile.response.GetAllProfileResponse;
import com.hart.overwatch.profile.response.GetFullProfileResponse;
import com.hart.overwatch.profile.response.GetProfilePackageResponse;
import com.hart.overwatch.profile.response.GetProfileResponse;
import com.hart.overwatch.profile.response.GetProfileVisibilityResponse;
import com.hart.overwatch.profile.response.RemoveAvatarResponse;
import com.hart.overwatch.profile.response.UpdateProfileResponse;
import com.hart.overwatch.profile.response.UpdateProfileVisibilityResponse;
import com.hart.overwatch.profile.response.UploadAvatarResponse;

@RestController
@RequestMapping(path = "/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;


    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PatchMapping(path = "/{profileId}/visibility")
    public ResponseEntity<UpdateProfileVisibilityResponse> updateProfileVisibility(
            @RequestBody UpdateProfileVisibilityRequest request,
            @PathVariable("profileId") Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateProfileVisibilityResponse(
                "success", profileService.updateProfileVisibility(request, profileId)));

    }

    @PatchMapping(path = "/{profileId}/avatar/remove")
    public ResponseEntity<RemoveAvatarResponse> removeAvatar(
            @PathVariable("profileId") Long profileId, @RequestBody RemoveAvatarRequest request) {
        this.profileService.removeAvatar(profileId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new RemoveAvatarResponse("success"));
    }

    @PatchMapping(path = "/{profileId}/avatar/update")
    public ResponseEntity<UploadAvatarResponse> uploadAvatar(
            @PathVariable("profileId") Long profileId, UploadAvatarRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UploadAvatarResponse("success",
                this.profileService.uploadAvatar(request, profileId)));
    }

    @PatchMapping(path = "/{profileId}")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @PathVariable("profileId") Long profileId, @RequestBody UpdateProfileRequest request) {

        this.profileService.updateProfile(profileId, request);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateProfileResponse("success"));
    }

    @GetMapping(path = "/{profileId}/populate")
    public ResponseEntity<GetProfileResponse> populateProfile(
            @PathVariable("profileId") Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetProfileResponse("success", this.profileService.populateProfile(profileId)));
    }

    @GetMapping(path = "/{profileId}/visibility")
    public ResponseEntity<GetProfileVisibilityResponse> getProfileVisibility(
            @PathVariable("profileId") Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetProfileVisibilityResponse("success",
                profileService.getProfileVisibility(profileId)));
    }

    @GetMapping(path = "/packages")
    public ResponseEntity<GetProfilePackageResponse> getProfilePackages(
            @RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetProfilePackageResponse("success",
                profileService.getProfilePackages(userId)));
    }

    @GetMapping(path = "/{profileId}")
    public ResponseEntity<GetFullProfileResponse> getProfile(
            @PathVariable("profileId") Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetFullProfileResponse("success", this.profileService.getProfile(profileId)));
    }



    @GetMapping(path = "/all/{filterType}")
    public ResponseEntity<GetAllProfileResponse> getAllProfiles(
            @PathVariable("filterType") String filterType, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllProfileResponse("success",
                this.profileService.getAllProfiles(filterType, page, pageSize, direction)));

    }

}
