package com.hart.overwatch.pexel;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.pexel.response.FetchPexelResponse;

@RestController
@RequestMapping(path = "/api/v1/pexels")
public class PexelController {

    private final PexelService pexelService;


    @Autowired
    public PexelController(PexelService pexelService) {
        this.pexelService = pexelService;
    }

    @GetMapping("")
    public ResponseEntity<FetchPexelResponse> getPexelPhotos(@RequestParam("query") String query)
            throws IOException  {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new FetchPexelResponse("success", pexelService.fetchPexelPhotos(query)));
    }
}
