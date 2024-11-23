package com.hart.overwatch.paymentmethod;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodListParams.Type;
import com.stripe.param.PaymentMethodListParams;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.paymentmethod.dto.UserPaymentMethodDto;
import com.hart.overwatch.paymentmethod.request.CreateConnectAccountRequest;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;
import com.hart.overwatch.paymentmethod.request.TransferCustomerMoneyToReviewerRequest;
import com.hart.overwatch.profile.dto.FullPackageDto;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.repository.RepositoryRepository;
import com.hart.overwatch.repository.RepositoryService;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.stripepaymentintent.StripePaymentIntentService;

@Service
public class UserPaymentMethodService {
    @Value("${STRIPE_RETURN_URL}")
    private String stripeReturnUrl;

    @Value("${STRIPE_REFRESH_URL}")
    private String stripeRefreshUrl;


    private final UserPaymentMethodRepository userPaymentMethodRepository;

    private final UserService userService;

    private final RepositoryService repositoryService;

    private final StripePaymentIntentService stripePaymentIntentService;

    @Autowired
    public UserPaymentMethodService(UserPaymentMethodRepository userPaymentMethodRepository,
            UserService userService, RepositoryService repositoryService,
            StripePaymentIntentService stripePaymentIntentService) {
        this.userPaymentMethodRepository = userPaymentMethodRepository;
        this.userService = userService;
        this.repositoryService = repositoryService;
        this.stripePaymentIntentService = stripePaymentIntentService;
    }

    private UserPaymentMethod getUserPaymentMethodById(Long userPaymentMethodId) {
        return this.userPaymentMethodRepository.findById(userPaymentMethodId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("A user payment method with the id %d was not found",
                                userPaymentMethodId)));
    }

    private boolean checkIfAlreadyACustomer(Long userId) {
        try {
            return this.userPaymentMethodRepository.getBooleanUserPaymentMethodByUserId(userId);

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


    private UserPaymentMethod getUserPaymentMethodByUserId(Long userId) {
        try {
            return this.userPaymentMethodRepository.getUserPaymentMethodByUserId(userId);

        } catch (DataAccessException ex) {
            return null;

        }
    }

    public UserPaymentMethodDto getUserPaymentMethods(Long userId) throws StripeException {
        try {
            UserPaymentMethod userPaymentMethod = getUserPaymentMethodByUserId(userId);
            Boolean stripeEnabled = false;

            if (userPaymentMethod == null) {
                throw new NotFoundException("A user payment method does not exist for this user");
            }

            if (userPaymentMethod.getUser().getRole() == Role.REVIEWER) {
                stripeEnabled = true;
                return new UserPaymentMethodDto(userPaymentMethod.getId(), null, null, null, null,
                        null, stripeEnabled);
            }


            PaymentMethodListParams params = PaymentMethodListParams.builder()
                    .setCustomer(userPaymentMethod.getStripeCustomerId())
                    .setType(PaymentMethodListParams.Type.CARD).build();

            List<PaymentMethod> stripePaymentMethods = PaymentMethod.list(params).getData();

            if (stripePaymentMethods.isEmpty()) {
                UserPaymentMethodDto emptyUserPaymentMethod = new UserPaymentMethodDto();
                emptyUserPaymentMethod.setStripeEnabled(stripeEnabled);
                return emptyUserPaymentMethod;
            }

            PaymentMethod stripePaymentMethod = stripePaymentMethods.get(0);

            stripeEnabled = true;
            return new UserPaymentMethodDto(userPaymentMethod.getId(),
                    stripePaymentMethod.getCard().getLast4(),
                    stripePaymentMethod.getCard().getBrand(),
                    stripePaymentMethod.getCard().getExpMonth(),
                    stripePaymentMethod.getCard().getExpYear(), userPaymentMethod.getName(),
                    stripeEnabled);


        } catch (StripeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void deleteUserPaymentMethod(Long id) throws StripeException {
        try {

            UserPaymentMethod userPaymentMethod = getUserPaymentMethodById(id);

            if (userPaymentMethod.getUser().getRole() == Role.REVIEWER) {
                Account resource = Account.retrieve(userPaymentMethod.getStripeConnectAccountId());
                resource.delete();
            } else {
                Customer customer = Customer.retrieve(userPaymentMethod.getStripeCustomerId());
                customer.delete();

            }
            this.userPaymentMethodRepository.delete(userPaymentMethod);

        } catch (StripeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private AccountLink generateAccountLink(String accountId) throws StripeException {
        AccountLinkCreateParams accountLinkParams = AccountLinkCreateParams.builder()
                .setAccount(accountId).setRefreshUrl(stripeRefreshUrl).setReturnUrl(stripeReturnUrl)
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING).build();

        return AccountLink.create(accountLinkParams);
    }

    private void saveReviewerDetails(User reviewer, String connectAccountId) {
        UserPaymentMethod userPaymentMethod = new UserPaymentMethod();
        userPaymentMethod.setUser(reviewer);
        userPaymentMethod.setStripeConnectAccountId(connectAccountId);

        userPaymentMethodRepository.save(userPaymentMethod);
    }

    public String createReviewerStripeAccount(CreateConnectAccountRequest request, Long userId)
            throws StripeException {
        User currentUser = userService.getCurrentlyLoggedInUser();
        String reviewerEmail = request.getEmail();
        if (!currentUser.getEmail().equals(reviewerEmail) || !currentUser.getId().equals(userId)) {
            throw new ForbiddenException(
                    "You are forbidden from doing this action of connecting an account");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("type", "express");
        params.put("email", reviewerEmail);

        Account account = Account.create(params);

        saveReviewerDetails(currentUser, account.getId());

        AccountLink accountLink = generateAccountLink(account.getId());
        return accountLink.getUrl();
    }


    private FullPackageDto retrievePackage(Repository repository, User reviewer) {
        FullPackageDto basic = reviewer.getProfile().getBasic();
        FullPackageDto standard = reviewer.getProfile().getStandard();
        FullPackageDto pro = reviewer.getProfile().getPro();

        List<FullPackageDto> packages = Arrays.asList(basic, standard, pro);

        packages = packages.stream().filter(
                pack -> Double.valueOf(pack.getPrice()).equals(repository.getPaymentPrice()))
                .collect(Collectors.toList());

        if (packages.isEmpty()) {
            throw new BadRequestException("Missing package selection");
        }
        return packages.get(0);
    }


    private PaymentIntent createPaymentIntent(String stripeCustomerId,
            String reviewerStripeAccountId, long applicationFee, FullPackageDto selectedPackage,
            Long ownerId, Long reviewerId) throws StripeException {

        double selectedPackagePrice = Double.parseDouble(selectedPackage.getPrice());
        long selectedPackagePriceInCents = Math.round(selectedPackagePrice * 100);

        String paymentMethodId = getPaymentMethodIdForCustomer(stripeCustomerId);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCustomer(stripeCustomerId).setDescription(selectedPackage.getDescription())
                .putMetadata("userId", ownerId.toString())
                .putMetadata("reviewerId", reviewerId.toString())
                .setAmount(selectedPackagePriceInCents).setCurrency("usd").setConfirm(true)
                .setTransferData(PaymentIntentCreateParams.TransferData.builder()
                        .setDestination(reviewerStripeAccountId)
                        .setAmount(selectedPackagePriceInCents - applicationFee).build())
                .setPaymentMethod(paymentMethodId)
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods
                        .builder().setEnabled(true).build())
                .setReturnUrl(stripeReturnUrl).build();

        return PaymentIntent.create(params);
    }

    @Transactional
    public PaymentIntent payoutReviewer(TransferCustomerMoneyToReviewerRequest request)
            throws StripeException {
        User reviewer = userService.getUserById(request.getReviewerId());
        User owner = userService.getUserById(request.getOwnerId());
        Repository repository = repositoryService.getRepositoryById(request.getRepositoryId());

        FullPackageDto selectedPackage = retrievePackage(repository, reviewer);


        String stripeCustomerId = owner.getUserPaymentMethods().get(0).getStripeCustomerId();
        String reviewerStripeAccountId =
                reviewer.getUserPaymentMethods().get(0).getStripeConnectAccountId();

        long applicationFee = calculatePlatformFee(
                Math.round(Double.parseDouble(selectedPackage.getPrice()) * 100));

        PaymentIntent paymentIntent = createPaymentIntent(stripeCustomerId, reviewerStripeAccountId,
                applicationFee, selectedPackage, owner.getId(), reviewer.getId());

        if ("succeeded".equals(paymentIntent.getStatus())) {
            repositoryService.updateStatus(request.getRepositoryId(), RepositoryStatus.PAID);
            stripePaymentIntentService.createStripePaymentIntent(owner, reviewer, paymentIntent,
                    applicationFee);
        }

        return paymentIntent;
    }

    private long calculatePlatformFee(long price) {
        return (long) (price * 0.10);
    }

    private String getPaymentMethodIdForCustomer(String stripeCustomerId) throws StripeException {
        PaymentMethodCollection paymentMethods = PaymentMethod.list(PaymentMethodListParams
                .builder().setCustomer(stripeCustomerId).setType(Type.CARD).build());

        if (!paymentMethods.getData().isEmpty()) {
            return paymentMethods.getData().get(0).getId();
        } else {
            throw new BadRequestException("No payment methods found for the customer");
        }
    }
}
