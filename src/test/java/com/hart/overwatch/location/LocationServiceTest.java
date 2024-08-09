package com.hart.overwatch.location;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;


@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LocationServiceTest {

    private final String GEOAPIFY_API_KEY = "dummy-api-key";

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserService userService;

    private User user;

    private Location location;

    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        location = new Location(user, "United States", "44 Main Street", "", "Chicago", "IL",
                "34122", "4444444444");

        user.setId(1L);
        location.setId(1L);
        user.setLocation(location);
    }


    @Test
    public void LocationService_GetLocationById_ReturnLocation() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        Location returnedLocation = locationService.getLocationById(location.getId());

        Assertions.assertThat(returnedLocation).isNotNull();
        Assertions.assertThat(returnedLocation.getId()).isEqualTo(location.getId());
    }

    @Test
    public void LocationService_GetLocationById_ThrowNotFoundException() {
        when(locationRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> locationService.getLocationById(2L)).isInstanceOf(NotFoundException.class)
            .hasMessage(String.format("A location with the id %d was not found", 2L));
    }

}


