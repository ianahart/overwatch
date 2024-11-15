package com.hart.overwatch.stripepaymentrefund;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.stripepaymentintent.PaymentIntentStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntent;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentService;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;
import com.hart.overwatch.stripepaymentrefund.request.CreateStripePaymentRefundRequest;
import com.hart.overwatch.stripepaymentrefund.request.UpdateStripePaymentRefundRequest;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

@Service
public class StripePaymentRefundService {

    private final StripePaymentRefundRepository stripePaymentRefundRepository;

    private final UserService userService;

    private final StripePaymentIntentService stripePaymentIntentService;

    private final PaginationService paginationService;

    public StripePaymentRefundService(StripePaymentRefundRepository stripePaymentRefundRepository,
            UserService userService, StripePaymentIntentService stripePaymentIntentService,
            PaginationService paginationService) {
        this.stripePaymentRefundRepository = stripePaymentRefundRepository;
        this.userService = userService;
        this.stripePaymentIntentService = stripePaymentIntentService;
        this.paginationService = paginationService;
    }

    private StripePaymentRefund getStripePaymentRefundById(Long stripePaymentRefundId) {
        return stripePaymentRefundRepository.findById(stripePaymentRefundId)



                .orElseThrow(() -> new NotFoundException(String.format(
                        "Could not find stripe payment refund with id %d", stripePaymentRefundId)));
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

    public PaginationDto<StripePaymentRefundDto> getPaymentRefunds(Long userId, int page,
            int pageSize, String direction) {

        User user = userService.getUserById(userId);

        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have priveleges to access this route");
        }

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<StripePaymentRefundDto> result =
                this.stripePaymentRefundRepository.findPaymentRefunds(pageable);


        return new PaginationDto<StripePaymentRefundDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());

    }

    private void rejectRefund(StripePaymentRefund stripePaymentRefund, String adminNotes) {
        stripePaymentRefund.setStatus(PaymentRefundStatus.REJECTED);
        stripePaymentRefund.setAdminNotes(adminNotes);

        stripePaymentRefundRepository.save(stripePaymentRefund);
    }

    @Transactional
    private void approveRefund(StripePaymentIntent stripePaymentIntent,
            StripePaymentRefund stripePaymentRefund, String adminNotes) {
        stripePaymentRefund.setStatus(PaymentRefundStatus.APPROVED);
        stripePaymentRefund.setAdminNotes(adminNotes);

        stripePaymentRefundRepository.save(stripePaymentRefund);

        stripePaymentIntentService.updateStatus(PaymentIntentStatus.REFUNDED,
                stripePaymentIntent.getId());

        String paymentIntentId = stripePaymentIntent.getPaymentIntentId();

        RefundCreateParams params =
                RefundCreateParams.builder().setPaymentIntent(paymentIntentId).build();

        try {
            Refund stripeRefund = Refund.create(params);
            stripePaymentRefund.setRefundId(stripeRefund.getId());

        } catch (StripeException e) {
            throw new BadRequestException("Unable to process transaction right now");
        }

        stripePaymentRefundRepository.save(stripePaymentRefund);
    }

    public void updatePaymentRefund(UpdateStripePaymentRefundRequest request, Long userId,
            Long paymentRefundId) {
        StripePaymentIntent stripePaymentIntent = stripePaymentIntentService
                .getStripePaymentIntentById(request.getStripePaymentIntentId());
        StripePaymentRefund stripePaymentRefund = getStripePaymentRefundById(paymentRefundId);
        String adminNotes = Jsoup.clean(request.getAdminNotes(), Safelist.none());

        if (request.getStatus().equals("reject")) {
            rejectRefund(stripePaymentRefund, adminNotes);
            return;
        }


        if (request.getStatus().equals("approve") && stripePaymentRefund.getRefundId() == null) {
            approveRefund(stripePaymentIntent, stripePaymentRefund, adminNotes);
        } else {
            throw new BadRequestException("They have already been refunded");
        }
    }
}
