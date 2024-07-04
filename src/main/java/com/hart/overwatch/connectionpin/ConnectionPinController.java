package com.hart.overwatch.connectionpin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.connectionpin.request.CreateConnectionPinRequest;
import com.hart.overwatch.connectionpin.response.CreateConnectionPinResponse;
import com.hart.overwatch.connectionpin.response.DeleteConnectionPinResponse;
import com.hart.overwatch.connectionpin.response.GetConnectionPinsResponse;

@RestController
@RequestMapping(path = "/api/v1/connection-pins")
public class ConnectionPinController {


    private final ConnectionPinService connectionPinService;

    @Autowired
    public ConnectionPinController(ConnectionPinService connectionPinService) {
        this.connectionPinService = connectionPinService;
    }


    @PostMapping(path = "")
    public ResponseEntity<CreateConnectionPinResponse> createConnectionPin(
            @RequestBody CreateConnectionPinRequest request) {
        this.connectionPinService.createConnectionPin(request.getOwnerId(), request.getPinnedId(),
                request.getConnectionId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateConnectionPinResponse("success"));
    }


    @GetMapping(path = "")
    public ResponseEntity<GetConnectionPinsResponse> getConnectionPins(
            @RequestParam("ownerId") Long ownerId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetConnectionPinsResponse("success",
                this.connectionPinService.getConnectionPins(ownerId)));
    }

    @DeleteMapping(path = "/{connectionPinId}")
    public ResponseEntity<DeleteConnectionPinResponse> deleteConnectionPin(
            @PathVariable("connectionPinId") Long connectionPinId) {
        this.connectionPinService.deletePinnedConnection(connectionPinId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteConnectionPinResponse("success"));
    }
}
