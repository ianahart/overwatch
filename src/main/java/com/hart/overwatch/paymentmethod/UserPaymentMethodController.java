package com.hart.overwatch.paymentmethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.paymentmethod.request.CreateConnectAccountRequest;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;
import com.hart.overwatch.paymentmethod.request.TransferCustomerMoneyToReviewerRequest;
import com.hart.overwatch.paymentmethod.response.CreateConnectAccountResponse;
import com.hart.overwatch.paymentmethod.response.CreateUserPaymentMethodResponse;
import com.hart.overwatch.paymentmethod.response.DeleteUserPaymentMethodResponse;
import com.hart.overwatch.paymentmethod.response.GetUserPaymentMethodResponse;
import com.hart.overwatch.paymentmethod.response.TransferCustomerMoneyToReviewerResponse;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;


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

    @GetMapping(path = "/users/{userId}/payment-methods")
    public ResponseEntity<GetUserPaymentMethodResponse> getUserPaymentMethods(
            @PathVariable("userId") Long userId) throws StripeException {
        return ResponseEntity.status(HttpStatus.OK).body(new GetUserPaymentMethodResponse("success",
                this.userPaymentMethodService.getUserPaymentMethods(userId)));
    }

    @DeleteMapping(path = "/payment-methods/{paymentMethodId}")
    public ResponseEntity<DeleteUserPaymentMethodResponse> deleteUserPaymentMethod(
            @PathVariable("paymentMethodId") Long paymentMethodId) throws StripeException {

        this.userPaymentMethodService.deleteUserPaymentMethod(paymentMethodId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteUserPaymentMethodResponse("success"));
    }

    @PostMapping(path = "/users/{userId}/payment-methods/connect")
    public ResponseEntity<CreateConnectAccountResponse> createReviewerStripeAccount(
            @RequestBody CreateConnectAccountRequest request, @PathVariable("userId") Long userId)
            throws StripeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateConnectAccountResponse(
                "success", userPaymentMethodService.createReviewerStripeAccount(request, userId)));
    }

    @PostMapping(path = "/payment-methods")
    public ResponseEntity<TransferCustomerMoneyToReviewerResponse> payoutReviewer(
            @Valid @RequestBody TransferCustomerMoneyToReviewerRequest request)
            throws StripeException {
        userPaymentMethodService.payoutReviewer(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new TransferCustomerMoneyToReviewerResponse("success"));
    }
}
