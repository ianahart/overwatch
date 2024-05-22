package com.hart.overwatch.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.location.response.GetLocationAutoCompleteResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class LocationController {


    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping(path = "/locations/autocomplete")
    public ResponseEntity<GetLocationAutoCompleteResponse> getAllLocations(
            @RequestParam("text") String text) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetLocationAutoCompleteResponse(
                "success", this.locationService.getLocationAutoComplete(text)));
    }
}
