package com.hart.overwatch.stripepaymentintent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.stripepaymentintent.response.GetAllStripePaymentIntentResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class StripePaymentIntentController {

    private final StripePaymentIntentService stripePaymentIntentService;

    public StripePaymentIntentController(StripePaymentIntentService stripePaymentIntentService) {
        this.stripePaymentIntentService = stripePaymentIntentService;
    }

    @GetMapping(path = "/users/{userId}/payment-intents")
    public ResponseEntity<GetAllStripePaymentIntentResponse> getStripePaymentIntents(
            @PathVariable("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetAllStripePaymentIntentResponse("success", stripePaymentIntentService
                        .getStripePaymentIntents(userId, page, pageSize, direction)));
    }

}
