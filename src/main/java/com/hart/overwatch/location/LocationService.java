package com.hart.overwatch.location;

import com.hart.overwatch.advice.NotFoundException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.UserService;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final UserService userService;


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
}
