package com.hart.overwatch.heartbeat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.heartbeat.response.HeartBeatResponse;

@RestController
@RequestMapping(path = "/api/v1/heartbeat")
public class HeartBeatController {

    public ResponseEntity<HeartBeatResponse> getHeartBeat() {
        return ResponseEntity.status(HttpStatus.OK).body(new HeartBeatResponse("success"));
    }
}
