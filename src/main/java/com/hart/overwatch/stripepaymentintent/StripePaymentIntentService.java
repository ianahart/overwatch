package com.hart.overwatch.stripepaymentintent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.stripe.model.PaymentIntent;

@Service
public class StripePaymentIntentService {

    private final UserService userService;

    private final StripePaymentIntentRepository stripePaymentIntentRepository;

    private final EmailQueueService emailQueueService;

    private final PaginationService paginationService;


    public StripePaymentIntentService(UserService userService,
            StripePaymentIntentRepository stripePaymentIntentRepository,
            EmailQueueService emailQueueService, PaginationService paginationService) {
        this.userService = userService;
        this.stripePaymentIntentRepository = stripePaymentIntentRepository;
        this.emailQueueService = emailQueueService;
        this.paginationService = paginationService;
    }

    public StripePaymentIntent getStripePaymentIntentById(Long stripePaymentIntentId) {
        return stripePaymentIntentRepository.findById(stripePaymentIntentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Could not find stripe payment intent with the id %d",
                                stripePaymentIntentId)));
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

    public PaginationDto<StripePaymentIntentDto> getStripePaymentIntents(Long userId, int page,
            int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<StripePaymentIntentDto> result = this.stripePaymentIntentRepository
                .getStripePaymentIntentsByUserId(pageable, userId);

        return new PaginationDto<StripePaymentIntentDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

    public void updateStatus(PaymentIntentStatus status, Long stripePaymentIntentId) {
        StripePaymentIntent stripePaymentIntent = getStripePaymentIntentById(stripePaymentIntentId);

        if (stripePaymentIntent != null) {
            stripePaymentIntent.setStatus(status);
            stripePaymentIntentRepository.save(stripePaymentIntent);
        }
    }
}
