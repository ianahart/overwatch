package com.hart.overwatch.connection;

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
import com.hart.overwatch.connection.request.CreateConnectionRequest;
import com.hart.overwatch.connection.response.CreateConnectionResponse;
import com.hart.overwatch.connection.response.DeleteConnectionResponse;
import com.hart.overwatch.connection.response.VerifyConnectionResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }


    @PostMapping(path = "")
    public ResponseEntity<CreateConnectionResponse> createConnection(
            @Valid @RequestBody CreateConnectionRequest request) {

        this.connectionService.createConnection(request.getReceiverId(), request.getSenderId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateConnectionResponse("success"));
    }

    @GetMapping(path = "/verify")
    public ResponseEntity<VerifyConnectionResponse> verifyConnection(
            @RequestParam("senderId") Long senderId, @RequestParam("receiverId") Long receiverId) {
        return ResponseEntity.status(HttpStatus.OK).body(new VerifyConnectionResponse("success",
                this.connectionService.verifyConnection(senderId, receiverId)));
    }

    @DeleteMapping(path = "/{connectionId}")
    public ResponseEntity<DeleteConnectionResponse> deleteConnection(@PathVariable("connectionId") Long connectionId) {
        this.connectionService.deleteConnectionById(connectionId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteConnectionResponse("success"));
    }
}
