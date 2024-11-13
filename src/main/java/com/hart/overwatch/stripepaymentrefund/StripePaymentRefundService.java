package com.hart.overwatch.stripepaymentrefund;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentService;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class StripePaymentRefundService {

    private final StripePaymentRefundRepository stripePaymentRefundRepository;

    private final UserService userService;

    private final StripePaymentIntentService stripePaymentIntentService;

    public StripePaymentRefundService(StripePaymentRefundRepository stripePaymentRefundRepository,
            UserService userService, StripePaymentIntentService stripePaymentIntentService) {
        this.stripePaymentRefundRepository = stripePaymentRefundRepository;
        this.userService = userService;
        this.stripePaymentIntentService = stripePaymentIntentService;
    }


    private boolean alreadyAskedForRefund(Long userId, Long stripePaymentIntentId) {
        return stripePaymentRefundRepository
                .findPaymentRefundByUserIdAndStripePaymentIntentId(userId, stripePaymentIntentId);
    }

    public void createPaymentRefund(CreateStripePaymentRefundRequest request) {
        User user = userService.getCurrentlyLoggedInUser();

        if (!user.getId().equals(request.getUserId())) {
            throw new ForbiddenException(
                    "Cannot ask for a payment refund when the payment is not yours");
        }

        if (alreadyAskedForRefund(user.getId(), request.getStripePaymentIntentId())) {
            throw new BadRequestException("You have already asked for a refund for this payment");
        }

        StripePaymentIntent stripePaymentIntent = stripePaymentIntentService
                .getStripePaymentIntentById(request.getStripePaymentIntentId());

        String reason = Jsoup.clean(request.getReason(), Safelist.none());

        StripePaymentRefund stripePaymentRefund = new StripePaymentRefund();
        stripePaymentRefund.setStripePaymentIntent(stripePaymentIntent);
        stripePaymentRefund.setUser(user);
        stripePaymentRefund.setAmount(stripePaymentIntent.getAmount());
        stripePaymentRefund.setCurrency(stripePaymentIntent.getCurrency());
        stripePaymentRefund.setReason(reason);
        stripePaymentRefund.setStatus(PaymentRefundStatus.PENDING);

        stripePaymentRefundRepository.save(stripePaymentRefund);

        stripePaymentIntentService.updateStatus(PaymentIntentStatus.PENDING,
                request.getStripePaymentIntentId());

    }
}
