package com.hart.overwatch.location;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
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
import com.hart.overwatch.location.request.CreateLocationRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.TestPropertySource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.ResponseBody;


@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LocationServiceTest {

    private final String GEOAPIFY_API_KEY = "dummy-api-key";

    @InjectMocks
    private LocationService locationService;


    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

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

    @Test
    public void LocationService_GetLocationAutoComplete_ReturnAddress() throws IOException {
        String mockResponseBody =
                "{\"features\":[{\"place\":\"Location 1\"},{\"place\":\"Location 2\"}]}";
        ResponseBody responseBody = ResponseBody.create(mockResponseBody, null);

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url(String.format(
                        "https://api.geoapify.com/v1/geocode/autocomplete?text=New%%20York&apiKey=%s",
                        GEOAPIFY_API_KEY)).build())
                .protocol(okhttp3.Protocol.HTTP_1_1).code(200).message("OK").body(responseBody)
                .build();

        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        String result = locationService.getLocationAutoComplete("New York");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result)
                .isEqualTo("[{\"place\":\"Location 1\"},{\"place\":\"Location 2\"}]");

        verify(mockClient, times(1)).newCall(any(Request.class));

    }

    @Test
    public void LocationService_CreateOrUpdateLocation_Create_ReturnNothing() {
        Long userId = user.getId();
        CreateLocationRequest request = new CreateLocationRequest("12 main street", "",
                "New York City", "United States", "4444444444", "NY", "11111");

        Location locationToSave = new Location(user, request.getCountry(), request.getAddress(),
                request.getAddressTwo(), request.getCity(), request.getState(),
                request.getZipCode(), request.getPhoneNumber());

        when(locationRepository.userLocationExists(userId)).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);
        when(locationRepository.save(any(Location.class))).thenReturn(locationToSave);

        locationService.createOrUpdateLocation(userId, request);

        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void LocationService_CreateOrUpdateLocation_Update_ReturnNothing() {
        Long userId = user.getId();
        CreateLocationRequest request = new CreateLocationRequest("12 main street", "",
                "New York City", "United States", "4444444444", "NY", "11111");

        when(locationRepository.userLocationExists(userId)).thenReturn(true);
        when(locationRepository.getLocationByUserId(userId)).thenReturn(location);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        locationService.createOrUpdateLocation(userId, request);

        verify(locationRepository, times(1)).save(any(Location.class));


    }
}


