package com.hart.overwatch.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.favorite.dto.MinFavoriteDto;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final UserService userService;

    private final ProfileService profileService;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, UserService userService,
            ProfileService profileService) {
        this.favoriteRepository = favoriteRepository;
        this.userService = userService;
        this.profileService = profileService;
    }

    private MinFavoriteDto getFavorite(User user, Profile profile) {
        return this.favoriteRepository.getFavoriteByUserIdAndProfileId(user.getId(),
                profile.getId());
    }

    private void findAndDeleteFavorite(User user, Profile profile) {
        MinFavoriteDto favorite = getFavorite(user, profile);

        this.favoriteRepository.deleteById(favorite.getId());

    }

    private void createFavorite(User user, Profile profile) {

        try {
            Favorite favorite = new Favorite(user, profile);

            this.favoriteRepository.save(favorite);

        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Already favorited this reviewer");
        }

    }

    public void toggleFavorite(Boolean isFavorited, Long userId, Long profileId) {
        User user = this.userService.getUserById(userId);
        Profile profile = this.profileService.getProfileById(profileId);

        if (!isFavorited) {
            createFavorite(user, profile);
        } else {
            findAndDeleteFavorite(user, profile);

        }
    }

    public Boolean favoriteExists(Long userId, Long profileId) {
        try {

            return this.favoriteRepository.favoriteExists(userId, profileId);

        } catch (DataAccessException ex) {

            return false;
        }
    }


    public Page<Long> getFavoriteIds(Pageable pageable, Long currentUserId) {
        try {
            return this.favoriteRepository.getCurrentUserFavoriteIds(pageable, currentUserId);

        } catch (DataAccessException ex) {

            return null;
        }
    }
}
