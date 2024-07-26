package com.hart.overwatch.favorite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.favorite.dto.MinFavoriteDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileService;
import com.hart.overwatch.profile.dto.AvailabilityDto;
import com.hart.overwatch.profile.dto.FullAvailabilityDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.PackageDto;
import com.hart.overwatch.profile.dto.WorkExpDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private UserService userService;

    private User user;

    private Profile profile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Boolean loggedIn = true;
        user = new User(Long.valueOf(1L), "john@mail.com", timestamp, timestamp, "John", "Doe",
                "John Doe", Role.USER, loggedIn);

        List<WorkExpDto> workExp = new ArrayList<>();
        List<ItemDto> languages = new ArrayList<>();
        List<ItemDto> programmingLanguages = new ArrayList<>();
        List<ItemDto> qualifications = new ArrayList<>();
        List<PackageDto> items = new ArrayList<>();
        List<FullAvailabilityDto> availability = new ArrayList<>();
        List<AvailabilityDto> slots = new ArrayList<>();
        slots.add(new AvailabilityDto("1", "10:00AM", "5:00PM"));
        availability.add(new FullAvailabilityDto("Thursday", slots));
        items.add(new PackageDto("1", "this is the name", 0));
        FullPackageDto basic = new FullPackageDto("19.99", "description", items);
        FullPackageDto standard = new FullPackageDto("29.99", "description", items);
        FullPackageDto pro = new FullPackageDto("39.99", "description", items);
        workExp.add(new WorkExpDto("1", "Software Engineer", "this is a description"));
        languages.add(new ItemDto("1", "English"));
        programmingLanguages.add(new ItemDto("1", "JavaScript"));
        qualifications.add(new ItemDto("1", "Computer Science Degree"));

        profile = new Profile(Long.valueOf(2L), timestamp, timestamp, "", "", "crestview school",
                workExp, "444-444-4444", "john@mail.com", "John Doe", "john072", "this is a bio",
                "this is a tagline", languages, programmingLanguages, qualifications, basic,
                standard, pro, availability, "moreInfo");

    }


    @Test
    public void should_create_favorite_when_not_favorited() {
        Boolean isFavorited = false;
        Long userId = user.getId();
        Long profileId = profile.getId();
        Favorite favorite = new Favorite(user, profile);

        when(userService.getUserById(userId)).thenReturn(user);
        when(profileService.getProfileById(profileId)).thenReturn(profile);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);


        favoriteService.toggleFavorite(isFavorited, userId, profileId);

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
        verify(favoriteRepository, never()).deleteById(anyLong());

    }

    @Test
    public void should_delete_favorite_when_favorited() {
        Boolean isFavorited = true;
        Favorite favorite = new Favorite(user, profile);
        favorite.setId(1L);

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(profileService.getProfileById(profile.getId())).thenReturn(profile);
        when(favoriteRepository.getFavoriteByUserIdAndProfileId(user.getId(), profile.getId()))
                .thenReturn(new MinFavoriteDto(1L));
        doNothing().when(favoriteRepository).deleteById(favorite.getId());

        favoriteService.toggleFavorite(isFavorited, user.getId(), profile.getId());

        verify(favoriteRepository, never()).save(any(Favorite.class));
        verify(favoriteRepository, times(1)).deleteById(favorite.getId());
    }


    @Test
    public void should_return_true_when_favorite_exists() {
        Favorite favorite = new Favorite(user, profile);
        Long userId = user.getId();
        Long profileId = profile.getId();
        favorite.setId(1L);

        when(favoriteRepository.favoriteExists(userId, profileId)).thenReturn(true);
        Boolean favoriteExists = favoriteService.favoriteExists(userId, profileId);
        assertTrue(favoriteExists);
    }

    @Test
    public void should_return_false_when_favorite_does_not_exist() {
        Long userId = 3L;
        Long profileId = 4L;

        when(favoriteRepository.favoriteExists(userId, profileId)).thenReturn(false);

        Boolean result = favoriteService.favoriteExists(userId, profileId);

        assertFalse(result);
    }

    @Test
    public void should_return_false_when_data_access_exception_is_thrown() {
        Long userId = 1L;
        Long profileId = 2L;

        when(favoriteRepository.favoriteExists(userId, profileId))
                .thenThrow(new DataAccessException("Database error") {});

        Boolean result = favoriteService.favoriteExists(userId, profileId);

        assertFalse(result);
    }

    @Test
    public void should_return_favorite_ids_when_successful() {
        Pageable pageable = Pageable.unpaged();
        Long currentUserId = user.getId();
        Page<Long> favoriteIds = new PageImpl<>(List.of(1L, 2L, 3L));

        when(favoriteRepository.getCurrentUserFavoriteIds(pageable, currentUserId))
                .thenReturn(favoriteIds);

        Page<Long> result = favoriteService.getFavoriteIds(pageable, currentUserId);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertTrue(result.getContent().containsAll(List.of(1L, 2L, 3L)));

    }

    @Test
    public void should_return_null_when_data_access_exception_is_thrown() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        Long currentUserId = 1L;

        when(favoriteRepository.getCurrentUserFavoriteIds(pageable, currentUserId))
                .thenThrow(new DataAccessException("Database error") {});

        // Act
        Page<Long> result = favoriteService.getFavoriteIds(pageable, currentUserId);

        // Assert
        assertNull(result);

    }
}
