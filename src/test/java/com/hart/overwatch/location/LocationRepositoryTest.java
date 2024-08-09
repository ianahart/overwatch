package com.hart.overwatch.location;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.hart.overwatch.location.dto.LocationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_location_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Location location;



    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        location = new Location(user, "United States", "44 Main Street", "", "Chicago", "IL",
                "34122", "4444444444");

        userRepository.save(user);
        locationRepository.save(location);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        locationRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void LocationRepository_GetFullLocationByUserId_ReturnLocationDto() {
        LocationDto locationDto = locationRepository.getFullLocationByUserId(1L);

        Assertions.assertThat(locationDto).isNotNull();
        Assertions.assertThat(locationDto.getAddress()).isEqualTo(location.getAddress());
        Assertions.assertThat(locationDto.getAddressTwo()).isEqualTo(location.getAddressTwo());
        Assertions.assertThat(locationDto.getCity()).isEqualTo(location.getCity());
        Assertions.assertThat(locationDto.getCountry()).isEqualTo(location.getCountry());
        Assertions.assertThat(locationDto.getPhoneNumber()).isEqualTo(location.getPhoneNumber());
        Assertions.assertThat(locationDto.getState()).isEqualTo(location.getState());
        Assertions.assertThat(locationDto.getZipCode()).isEqualTo(location.getZipCode());
    }

    @Test
    public void LocationRepository_UserLocationExists_ReturnBoolean() {
        boolean exists = locationRepository.userLocationExists(user.getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void LocationRepository_GetLocationByUserId_ReturnLocation() {
        Location returnedLocation = locationRepository.getLocationByUserId(1L);

        Assertions.assertThat(returnedLocation).isNotNull();
        Assertions.assertThat(returnedLocation.getId()).isEqualTo(location.getId());
    }

    @Test
    public void LocationRepository_CreateLocation_ReturnLocation() {
        Location newLocation = new Location(new User(), "United States", "76 Rock Street", "",
                "New York City", "NY", "11111", "2222222222");

        Location savedLocation = locationRepository.save(newLocation);

        Assertions.assertThat(savedLocation).isNotNull();
        Assertions.assertThat(savedLocation.getId()).isEqualTo(2L);
        Assertions.assertThat(savedLocation.getCity()).isEqualTo(newLocation.getCity());
    }

    @Test
    public void LocationRepository_FindLocationById_ReturnLocation() {
        Optional<Location> returnedLocation = locationRepository.findById(1L);

        Assertions.assertThat(returnedLocation).isNotNull();
        Assertions.assertThat(returnedLocation.get().getCity()).isEqualTo(location.getCity());
    }
}


