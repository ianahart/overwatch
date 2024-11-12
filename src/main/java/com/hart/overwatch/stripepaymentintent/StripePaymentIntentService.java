package com.hart.overwatch.stripepaymentintent;

import org.springframework.stereotype.Service;
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.stripe.model.PaymentIntent;

@Service
public class StripePaymentIntentService {

    private final UserService userService;

    private final StripePaymentIntentRepository stripePaymentIntentRepository;

    private final EmailQueueService emailQueueService;


    public StripePaymentIntentService(UserService userService,
            StripePaymentIntentRepository stripePaymentIntentRepository,
            EmailQueueService emailQueueService) {
        this.userService = userService;
        this.stripePaymentIntentRepository = stripePaymentIntentRepository;
        this.emailQueueService = emailQueueService;
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
        String reviewerText =
                String.format("%s has paid you $%s %s for reviewing their code!\n package:%s",
                        user.getFullName(), convertCentsToDollars(paymentIntent.getAmount()),
                        paymentIntent.getCurrency(), paymentIntent.getDescription());
        String userText = String.format("You paid %s $%s %s for reviewing your code!\n package: %s",
                reviewer.getFullName(), convertCentsToDollars(paymentIntent.getAmount()),
                paymentIntent.getCurrency(), paymentIntent.getDescription());
        queueEmail(reviewer, "Payment Recieved", reviewerText);
        queueEmail(user, "Payment Notification", userText);
    }

    private String convertCentsToDollars(Long cents) {
        double dollars = cents / 100.0;

        return String.format("$%.2f", dollars);
    }

    private void queueEmail(User recipient, String subject, String body) {
        if (body != null && !body.isEmpty()) {
            EmailRequest emailRequest = new EmailRequest(recipient.getEmail(), subject, body);
            emailQueueService.queueEmail(emailRequest);
        }
    }
}
