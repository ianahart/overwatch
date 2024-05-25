package com.hart.overwatch.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.profile.request.RemoveAvatarRequest;
import com.hart.overwatch.profile.request.UploadAvatarRequest;
import com.hart.overwatch.profile.response.RemoveAvatarResponse;
import com.hart.overwatch.profile.response.UploadAvatarResponse;

@RestController
@RequestMapping(path = "/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;


    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
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

}
