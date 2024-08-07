package com.hart.overwatch.phone;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.rest.verify.v2.service.VerificationCheckCreator;
import com.twilio.rest.verify.v2.service.VerificationCreator;
import org.springframework.beans.factory.annotation.Value;


@ExtendWith(MockitoExtension.class)
public class PhoneServiceTest {

    @Value("${TWILIO_TEST_ACCOUNT_SID}")
    private String TWILIO_TEST_ACCOUNT_SID;

    @Value("${TWILIO_TEST_AUTH_TOKEN}")
    private String TWILIO_TEST_AUTH_TOKEN;

    @Value("${TWILIO_VERIFICATION_SID}")
    private String TWILIO_VERIFICATION_SID;

    @InjectMocks
    private PhoneService phoneService;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private UserService userService;

    private User user;
    private Phone phone;

    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        Boolean isVerified = false;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        phone = new Phone("4444444444", isVerified, user);

        user.setPhones(Arrays.asList(phone));
    }


    @Test
    public void PhoneService_VerifyUserOTP_ReturnBoolean() throws Exception {
        try (MockedStatic<Twilio> mockedTwilio = mockStatic(Twilio.class);
                MockedStatic<VerificationCheck> mockedVerificationCheck =
                        mockStatic(VerificationCheck.class)) {

            mockedTwilio.when(() -> Twilio.init(TWILIO_TEST_ACCOUNT_SID, TWILIO_TEST_AUTH_TOKEN))
                    .thenAnswer(invocation -> null);

            VerificationCheckCreator mockVerificationCheckCreator =
                    mock(VerificationCheckCreator.class);
            VerificationCheck mockVerificationCheck = mock(VerificationCheck.class);

            when(mockVerificationCheckCreator.setTo(anyString()))
                    .thenReturn(mockVerificationCheckCreator);
            when(mockVerificationCheckCreator.setCode(anyString()))
                    .thenReturn(mockVerificationCheckCreator);
            when(mockVerificationCheckCreator.create()).thenReturn(mockVerificationCheck);

            when(mockVerificationCheck.getStatus()).thenReturn("approved");

            mockedVerificationCheck.when(() -> VerificationCheck.creator(TWILIO_VERIFICATION_SID))
                    .thenReturn(mockVerificationCheckCreator);

            boolean result = phoneService.verifyUserOTP(user, "123456");

            assertTrue(result);

        }
    }

    @Test
    public void PhoneService_GenerateUserOTP_ReturnString() {
        try (MockedStatic<Twilio> mockedTwilio = mockStatic(Twilio.class);
                MockedStatic<Verification> mockedVerification = mockStatic(Verification.class)) {

            mockedTwilio.when(() -> Twilio.init(TWILIO_TEST_ACCOUNT_SID, TWILIO_TEST_AUTH_TOKEN))
                    .thenAnswer(invocation -> null);

            when(userService.getUserById(user.getId())).thenReturn(user);

            VerificationCreator mockVerificationCreator = mock(VerificationCreator.class);
            Verification mockVerification = mock(Verification.class);
            String phoneNumber = user.getPhones().get(0).getPhoneNumber();

            mockedVerification.when(
                    () -> Verification.creator(TWILIO_VERIFICATION_SID, "+1" + phoneNumber, "sms"))
                    .thenReturn(mockVerificationCreator);

            when(mockVerificationCreator.create()).thenReturn(mockVerification);

            phoneService.generateUserOTP(user.getId());

            mockedTwilio.verify(() -> Twilio.init(TWILIO_TEST_ACCOUNT_SID, TWILIO_TEST_AUTH_TOKEN));

            mockedVerification.verify(
                    () -> Verification.creator(TWILIO_VERIFICATION_SID, "+1" + phoneNumber, "sms"));

            verify(mockVerificationCreator).create();
        }
    }

    @Test
    public void PhoneService_CreatePhone_ReturnNothing() {
        user.setId(1L);

        when(phoneRepository.existsByUserId(user.getId())).thenReturn(false);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(phoneRepository.save(any(Phone.class))).thenReturn(phone);

        assertDoesNotThrow(() -> phoneService.createPhone(user.getId(), "4444444444"));
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    public void PhoneService_CreatePhone_ThrowBadRequestException() {
        user.setId(1L);
        when(phoneRepository.existsByUserId(user.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> phoneService.createPhone(user.getId(), "4444444444"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Phone has already been added to our system");

    }

    @Test
    public void PhoneService_CreatePhone_ThrowForbiddenException() {
        user.setId(1L);
        when(phoneRepository.existsByUserId(2L)).thenReturn(false);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        Assertions.assertThatThrownBy(() -> phoneService.createPhone(2L, "4444444444"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot alter settings that are not yours");
    }
}

