package com.hart.overwatch.paymentmethod;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;

@Service
public class UserPaymentMethodService {

    private final UserPaymentMethodRepository userPaymentMethodRepository;
    private final UserService userService;

    @Autowired
    public UserPaymentMethodService(UserPaymentMethodRepository userPaymentMethodRepository,
            UserService userService) {
        this.userPaymentMethodRepository = userPaymentMethodRepository;
        this.userService = userService;
    }

    private UserPaymentMethod getUserPaymentMethodById(Long userPaymentMethodId) {
        return this.userPaymentMethodRepository.findById(userPaymentMethodId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("A user payment method with the id %d was not found",
                                userPaymentMethodId)));
    }

    private boolean checkIfAlreadyACustomer(Long userId) {
        try {
            return this.userPaymentMethodRepository.getUserPaymentMethodByUserId(userId);

        } catch (DataAccessException ex) {
            return true;
        }
    }

    public void createUserPaymentMethod(Long userId, CreateUserPaymentMethodRequest request)
            throws StripeException {

        try {
            User user = this.userService.getCurrentlyLoggedInUser();

            if (checkIfAlreadyACustomer(userId)) {
                throw new BadRequestException("User is already a stripe customer");
            }

            CustomerCreateParams params = CustomerCreateParams.builder().setName(request.getName())
                    .setEmail(user.getEmail()).build();

            Customer customer = Customer.create(params);
            String stripeCustomerId = customer.getId();

            PaymentMethodAttachParams attachParams =
                    PaymentMethodAttachParams.builder().setCustomer(stripeCustomerId).build();

            PaymentMethod paymentMethod = PaymentMethod.retrieve(request.getId());
            paymentMethod.attach(attachParams);

            UserPaymentMethod userPaymentMethod =
                    new UserPaymentMethod(user, Jsoup.clean(request.getCity(), Safelist.none()),
                            Jsoup.clean(request.getCountry(), Safelist.none()),
                            Jsoup.clean(request.getLine1(), Safelist.none()),
                            Jsoup.clean(request.getLine2(), Safelist.none()),
                            Jsoup.clean(request.getPostalCode(), Safelist.none()),
                            Jsoup.clean(request.getName(), Safelist.none()),
                            request.getDisplayBrand(), request.getType(), request.getExpMonth(),
                            request.getExpYear(), stripeCustomerId);


            this.userPaymentMethodRepository.save(userPaymentMethod);

        } catch (StripeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
