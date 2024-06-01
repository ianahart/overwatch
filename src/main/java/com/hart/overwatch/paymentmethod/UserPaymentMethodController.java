package com.hart.overwatch.paymentmethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;
import com.hart.overwatch.paymentmethod.response.CreateUserPaymentMethodResponse;
import com.stripe.exception.StripeException;


@RestController
@RequestMapping(path = "/api/v1")
public class UserPaymentMethodController {

    private final UserPaymentMethodService userPaymentMethodService;

    @Autowired
    public UserPaymentMethodController(UserPaymentMethodService userPaymentMethodService) {
        this.userPaymentMethodService = userPaymentMethodService;
    }

    @PostMapping(path = "/users/{userId}/payment-methods")
    public ResponseEntity<CreateUserPaymentMethodResponse> createUserPaymentMethod(
            @PathVariable("userId") Long userId,
            @RequestBody CreateUserPaymentMethodRequest request) throws StripeException {
        this.userPaymentMethodService.createUserPaymentMethod(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateUserPaymentMethodResponse("success"));
    }
}
