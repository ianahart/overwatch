package com.hart.overwatch.favorite;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.hart.overwatch.favorite.dto.MinFavoriteDto;
import com.hart.overwatch.profile.dto.AvailabilityDto;
import com.hart.overwatch.profile.dto.FullAvailabilityDto;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.profile.dto.ItemDto;
import com.hart.overwatch.profile.dto.PackageDto;
import com.hart.overwatch.profile.dto.WorkExpDto;
import com.hart.overwatch.setting.Setting;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private User user1;

    private User user2;

    private Profile profile;

    private Favorite favorite;

    @BeforeEach
    public void setUp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

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

        profile = new Profile(timestamp, timestamp, "", "", "crestview school", workExp,
                "444-444-4444", "jane@mail.com", "Jane Doe", "jane072", "this is a bio",
                "this is a tagline", languages, programmingLanguages, qualifications, basic,
                standard, pro, availability, "moreInfo");


        Boolean loggedIn = true;
        user1 = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user2 = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());


        this.userRepository.save(user1);
        this.userRepository.save(user2);
        this.profileRepository.save(profile);

        favorite = new Favorite(user1, profile);

        this.favoriteRepository.save(favorite);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing  down the test data....");
        this.favoriteRepository.deleteAll();
        this.profileRepository.deleteAll();
        this.userRepository.deleteAll();
        resetSequences();

    }

    private void resetSequences() {
        System.out.println("Resetting sequences");
        jdbcTemplate.execute("ALTER SEQUENCE favorite_sequence RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE profile_sequence RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE _user_sequence RESTART WITH 1");
    }

    @Test
    public void FavoriteRepository_GetFavoriteByUserIdAndProfileId_ReturnMinFavoriteDto() {

        MinFavoriteDto result =
                favoriteRepository.getFavoriteByUserIdAndProfileId(user1.getId(), profile.getId());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(favorite.getId());
    }

    @Test
    public void FavoriteRepository_FavoriteExists_ReturnBoolean() {
        Boolean exists =
                favoriteRepository.favoriteExists(user1.getId(), user2.getProfile().getId());
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void FavoriteRepository_GetCurrentUserFavoriteIds_ReturnPageOfIds() {
        User user3 = new User("smith@mail.com", "John", "Smith", "John Smith", Role.REVIEWER, true,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(user3);

        favoriteRepository.save(new Favorite(user1, user3.getProfile()));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Long> ids = favoriteRepository.getCurrentUserFavoriteIds(pageable, user1.getId());
        Assertions.assertThat(ids).isNotEmpty();
        Assertions.assertThat(ids.getContent()).contains(user3.getProfile().getId());
    }
}

