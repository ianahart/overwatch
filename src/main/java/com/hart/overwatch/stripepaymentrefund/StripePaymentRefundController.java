package com.hart.overwatch.stripepaymentrefund;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.response.CreateStripePaymentRefundResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/payment-refunds")
public class StripePaymentRefundController {

    private final StripePaymentRefundService stripePaymentRefundService;


    public StripePaymentRefundController(StripePaymentRefundService stripePaymentRefundService) {
        this.stripePaymentRefundService = stripePaymentRefundService;
    }

    @PostMapping("")
    public ResponseEntity<CreateStripePaymentRefundResponse> createPaymentRefund(
            @Valid @RequestBody CreateStripePaymentRefundRequest request) {
        stripePaymentRefundService.createPaymentRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateStripePaymentRefundResponse("success"));
    }
}
