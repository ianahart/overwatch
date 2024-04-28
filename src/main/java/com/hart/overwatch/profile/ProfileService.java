package com.hart.overwatch.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
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
}
