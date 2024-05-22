package com.hart.overwatch.location;

import com.hart.overwatch.advice.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.UserService;

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



    @Autowired
    public LocationService(LocationRepository locationRepository, UserService userService) {
        this.locationRepository = locationRepository;
        this.userService = userService;
    }


    public Location getLocationById(Long locationId) {
        return this.locationRepository.findById(locationId).orElseThrow(() -> new NotFoundException(
                String.format("A location with the id %d was not found", locationId)));
    }



    public void createOrUpdateLocation() {
        System.out.println("createOrUpdateLocation()");
    }

    public String getLocationAutoComplete(String text) {

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.geoapify.com/v1/geocode/autocomplete?text=" + text + "&apiKey="
                + GEOAPIFY_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            // Get response body
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
}
