package com.hart.overwatch.stripepaymentintent;

import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.stripe.model.PaymentIntent;

@Service
public class StripePaymentIntentService {

    private final UserService userService;

    private final StripePaymentIntentRepository stripePaymentIntentRepository;


    public StripePaymentIntentService(UserService userService,
            StripePaymentIntentRepository stripePaymentIntentRepository) {
        this.userService = userService;
        this.stripePaymentIntentRepository = stripePaymentIntentRepository;
    }


    public void createStripePaymentIntent(User user, User reviewer, PaymentIntent paymentIntent,
            Long applicationFee) {
        StripePaymentIntent stripePaymentIntent = new StripePaymentIntent();
        stripePaymentIntent.setUser(user);
        stripePaymentIntent.setReviewer(reviewer);
        stripePaymentIntent.setCurrency(paymentIntent.getCurrency());
        stripePaymentIntent.setPaymentIntentId(paymentIntent.getId());
        stripePaymentIntent.setAmount(paymentIntent.getAmount());
        stripePaymentIntent.setApplicationFee(applicationFee);
        stripePaymentIntent.setStatus(PaymentIntentStatus.PAID);
        stripePaymentIntent.setClientSecret(paymentIntent.getClientSecret());
        stripePaymentIntent.setDescription(paymentIntent.getDescription());

        stripePaymentIntentRepository.save(stripePaymentIntent);
    }
}
