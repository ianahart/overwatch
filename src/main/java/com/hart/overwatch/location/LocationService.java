package com.hart.overwatch.location;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.location.dto.LocationDto;
import com.hart.overwatch.location.request.CreateLocationRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import jakarta.validation.ConstraintViolationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;


@Service
public class LocationService {

    @Value("${GEOAPIFY_API_KEY}")
    private String GEOAPIFY_API_KEY;


    private final LocationRepository locationRepository;

    private final UserService userService;

    private final OkHttpClient client;



    @Autowired
    public LocationService(LocationRepository locationRepository, UserService userService, OkHttpClient client) {
        this.locationRepository = locationRepository;
        this.userService = userService;
        this.client = client;
    }


    public Location getLocationById(Long locationId) {
        return this.locationRepository.findById(locationId).orElseThrow(() -> new NotFoundException(
                String.format("A location with the id %d was not found", locationId)));
    }



    public String getLocationAutoComplete(String text) {

        String url = "https://api.geoapify.com/v1/geocode/autocomplete?text=" + text + "&apiKey="
                + GEOAPIFY_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JSONObject obj = new JSONObject(responseBody);
            JSONArray featuresArray = obj.getJSONArray("features");

            JSONArray resultArray = new JSONArray();

            for (int i = 0; i < featuresArray.length(); i++) {
                resultArray.put(featuresArray.getJSONObject(i));
            }

            return resultArray.toString();


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean userLocationExists(Long userId) {
        try {
            return this.locationRepository.userLocationExists(userId);

        } catch (DataAccessException ex) {
            return true;
        }
    }

    private String cleanString(String input) {
        if (input == null) {
            return "";
        } else {
            return Jsoup.clean(input, Safelist.none());
        }
    }

    private void createLocation(Long userId, CreateLocationRequest request) {
        try {
            User user = this.userService.getUserById(userId);

            Location location = new Location(user, cleanString(request.getCountry()),
                    cleanString(request.getAddress()), cleanString(request.getAddressTwo()),
                    cleanString(request.getCity()), cleanString(request.getState()),
                    cleanString(request.getZipCode()), request.getPhoneNumber());


            this.locationRepository.save(location);

        } catch (ConstraintViolationException ex) {

            ex.printStackTrace();
        }

    }

    private Location getLocationByUserId(Long userId) {
        try {
            return this.locationRepository.getLocationByUserId(userId);

        } catch (DataAccessException ex) {

            return null;
        }
    }

    private void updateLocation(Long userId, CreateLocationRequest request) {
        try {
            Location location = getLocationByUserId(userId);
            location.setCountry(cleanString(request.getCountry()));
            location.setAddress(cleanString(request.getAddress()));
            location.setAddressTwo(cleanString(request.getAddressTwo()));
            location.setCity(cleanString(request.getCity()));
            location.setState(cleanString(request.getState()));
            location.setZipCode(cleanString(request.getZipCode()));
            location.setPhoneNumber(request.getPhoneNumber());

            this.locationRepository.save(location);

        } catch (ConstraintViolationException ex) {
            ex.printStackTrace();
        }
    }

    public void createOrUpdateLocation(Long userId, CreateLocationRequest request) {
        if (userLocationExists(userId)) {
            updateLocation(userId, request);
        } else {
            createLocation(userId, request);
        }

        System.out.println(userId);
    }


    public LocationDto getFullLocationByUserId(Long userId) {
        try {
            return this.locationRepository.getFullLocationByUserId(userId);
        } catch (DataAccessException ex) {
            return null;
        }
    }
}
