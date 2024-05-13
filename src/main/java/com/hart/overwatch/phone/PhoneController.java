package com.hart.overwatch.phone;

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
import com.hart.overwatch.phone.request.CreatePhoneRequest;
import com.hart.overwatch.phone.response.CreatePhoneResponse;
import com.hart.overwatch.phone.response.DeletePhoneResponse;
import com.hart.overwatch.phone.response.GetPhoneResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/phones")
public class PhoneController {

    private final PhoneService phoneService;


    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("")
    public ResponseEntity<CreatePhoneResponse> createPhone(
            @Valid @RequestBody CreatePhoneRequest request) {
        this.phoneService.createPhone(request.getUserId(), request.getPhoneNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatePhoneResponse("success"));
    }

    @GetMapping("")
    public ResponseEntity<GetPhoneResponse> getPhone(@RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetPhoneResponse("success", this.phoneService.getPhone(userId)));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<DeletePhoneResponse> deletePhone(@PathVariable("phoneId") Long phoneId) {
        this.phoneService.deletePhone(phoneId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeletePhoneResponse("success"));
    }

}
