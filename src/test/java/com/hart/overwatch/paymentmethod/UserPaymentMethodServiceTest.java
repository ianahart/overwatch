package com.hart.overwatch.paymentmethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.paymentmethod.dto.UserPaymentMethodDto;
import com.hart.overwatch.paymentmethod.request.CreateUserPaymentMethodRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.TestPropertySource;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodListParams;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserPaymentMethodServiceTest {

    @InjectMocks
    private UserPaymentMethodService userPaymentMethodService;

    @Mock
    private UserPaymentMethodRepository userPaymentMethodRepository;

    @Mock
    private UserService userService;

    @Mock
    private Customer mockCustomer;

    @Mock
    private PaymentMethod mockPaymentMethod;

    private User user;

    private UserPaymentMethod userPaymentMethod;

    private UserPaymentMethod createUserPaymentMethod(User user) {
        UserPaymentMethod userPaymentMethod =
                new UserPaymentMethod(user, "New York City", "United States", "line 1", "line 2",
                        "03212", "John Doe", "visa", "card", 3, 2028, "dummy_stripe_customer_id");
        userPaymentMethod.setId(1L);
        userPaymentMethod.setUser(user);

        return userPaymentMethod;
    }

    private User createUser() {
        Boolean loggedIn = false;
        User user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user.setId(1L);

        return user;
    }


    @BeforeEach
    void setUp() {
        user = createUser();
        userPaymentMethod = createUserPaymentMethod(user);
        user.setUserPaymentMethods(List.of(userPaymentMethod));
    }

    @Test
    public void UserPaymentMethodService_CreateUserPaymentMethod_ThrowBadRequestException() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(userPaymentMethodRepository.getBooleanUserPaymentMethodByUserId(user.getId())).thenReturn(true);

        CreateUserPaymentMethodRequest request = new CreateUserPaymentMethodRequest("dummy_id", "New York City", "United States", "line 1", "line 2", "03245", user.getFullName(), "visa", "card", 3, 28);

        Assertions.assertThatThrownBy(() -> {
           userPaymentMethodService.createUserPaymentMethod(user.getId(), request);
        }).isInstanceOf(BadRequestException.class).hasMessage("User is already a stripe customer");
    }

    @Test
    public void UserPaymentMethodService_CreateUserPaymentMethod_ReturnNothing()
            throws StripeException {
        // Arrange
        Long userId = 1L;
        CreateUserPaymentMethodRequest request = new CreateUserPaymentMethodRequest();
        request.setName("John Doe");
        request.setId("pm_12345");
        request.setCity("Sample City");
        request.setCountry("Sample Country");
        request.setLine1("123 Main St");
        request.setLine2("Apt 4B");
        request.setPostalCode("12345");
        request.setDisplayBrand("Visa");
        request.setType("card");
        request.setExpMonth(12);
        request.setExpYear(2024);

        User mockUser = new User();
        mockUser.setEmail("user@example.com");

        when(userService.getCurrentlyLoggedInUser()).thenReturn(mockUser);

        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn("cus_12345");

        try (MockedStatic<Customer> mockedCustomer = mockStatic(Customer.class)) {
            mockedCustomer.when(() -> Customer.create(any(CustomerCreateParams.class)))
                    .thenReturn(mockCustomer);

            PaymentMethod mockPaymentMethod = mock(PaymentMethod.class);
            try (MockedStatic<PaymentMethod> mockedPaymentMethod =
                    mockStatic(PaymentMethod.class)) {
                mockedPaymentMethod.when(() -> PaymentMethod.retrieve("pm_12345"))
                        .thenReturn(mockPaymentMethod);

                userPaymentMethodService.createUserPaymentMethod(userId, request);

                verify(userPaymentMethodRepository, times(1)).save(any(UserPaymentMethod.class));
            }
        }
    }

    @Test
    public void UserPaymentMethodService_GetUserPaymentMethods_ReturnUserPaymentMethodDto() {
        when(userPaymentMethodRepository.getUserPaymentMethodByUserId(999L)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> {
           userPaymentMethodService.getUserPaymentMethods(999L);
        }).isInstanceOf(NotFoundException.class).hasMessage("A user payment method does not exist for this user");
    }

    @Test
    public void UserPaymenetMethodService_GetUserPaymentMethods_ReturnUserPaymentMethodDto() throws StripeException {
        when(userPaymentMethodRepository.getUserPaymentMethodByUserId(user.getId())).thenReturn(userPaymentMethod);

        PaymentMethod.Card mockCard = mock(PaymentMethod.Card.class);
        when(mockCard.getLast4()).thenReturn("4242");
        when(mockCard.getBrand()).thenReturn("Visa");
        when(mockCard.getExpMonth()).thenReturn(3L);
        when(mockCard.getExpYear()).thenReturn(2028L);

           PaymentMethod mockPaymentMethod = mock(PaymentMethod.class);
           when(mockPaymentMethod.getCard()).thenReturn(mockCard);

    PaymentMethodCollection mockPaymentMethodCollection = mock(PaymentMethodCollection.class);
    when(mockPaymentMethodCollection.getData()).thenReturn(List.of(mockPaymentMethod));

    try (MockedStatic<PaymentMethod> mockedStaticPaymentMethod = mockStatic(PaymentMethod.class)) {
        mockedStaticPaymentMethod.when(() -> PaymentMethod.list(any(PaymentMethodListParams.class)))
                                 .thenReturn(mockPaymentMethodCollection);

        UserPaymentMethodDto result = userPaymentMethodService.getUserPaymentMethods(user.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(userPaymentMethod.getId());
        Assertions.assertThat(result.getExpYear()).isEqualTo(userPaymentMethod.getExpYear().intValue());
        Assertions.assertThat(result.getExpMonth()).isEqualTo(userPaymentMethod.getExpMonth().intValue());
        Assertions.assertThat(result.getName()).isEqualTo(userPaymentMethod.getName());
   }
}

    @Test
    public void UserPaymentMethodService_DeleteUserPaymentMethod_ReturnNothing() throws StripeException {
        when(userPaymentMethodRepository.findById(userPaymentMethod.getId())).thenReturn(Optional.of(userPaymentMethod));

        try (MockedStatic<Customer> mockedStaticCustomer = mockStatic(Customer.class)) {
            Customer mockCustomer = mock(Customer.class);
            mockedStaticCustomer.when(() -> Customer.retrieve(userPaymentMethod.getStripeCustomerId())).thenReturn(mockCustomer);


            userPaymentMethodService.deleteUserPaymentMethod(userPaymentMethod.getId());

            verify(mockCustomer, times(1)).delete();

            verify(userPaymentMethodRepository, times(1)).delete(userPaymentMethod);
    }
}
}


