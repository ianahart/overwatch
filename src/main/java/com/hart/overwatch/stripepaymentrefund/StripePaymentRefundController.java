package com.hart.overwatch.stripepaymentrefund;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.request.UpdateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.response.CreateStripePaymentRefundResponse;
import com.hart.overwatch.stripepaymentrefund.response.GetAllStripePaymentRefundsResponse;
import com.hart.overwatch.stripepaymentrefund.response.UpdateStripePaymentRefundResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/")
public class StripePaymentRefundController {

    private final StripePaymentRefundService stripePaymentRefundService;


    public StripePaymentRefundController(StripePaymentRefundService stripePaymentRefundService) {
        this.stripePaymentRefundService = stripePaymentRefundService;
    }

    @PostMapping(path = "/payment-refunds")
    public ResponseEntity<CreateStripePaymentRefundResponse> createPaymentRefund(
            @Valid @RequestBody CreateStripePaymentRefundRequest request) {
        stripePaymentRefundService.createPaymentRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateStripePaymentRefundResponse("success"));
    }

    @GetMapping(path = "/admin/{userId}/payment-refunds")
    public ResponseEntity<GetAllStripePaymentRefundsResponse> getPaymentRefunds(
            @PathVariable("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllStripePaymentRefundsResponse(
                "success",
                stripePaymentRefundService.getPaymentRefunds(userId, page, pageSize, direction)));
    }


    @PatchMapping(path = "/admin/{userId}/payment-refunds/{paymentRefundId}")
    public ResponseEntity<UpdateStripePaymentRefundResponse> updatePaymentRefund(
            @Valid @RequestBody UpdateStripePaymentRefundRequest request,
            @PathVariable("userId") Long userId,
            @PathVariable("paymentRefundId") Long paymentRefundId) {

        stripePaymentRefundService.updatePaymentRefund(request, userId, paymentRefundId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateStripePaymentRefundResponse("success"));
    }
}
