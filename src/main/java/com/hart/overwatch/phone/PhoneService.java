package com.hart.overwatch.phone;


import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.phone.dto.PhoneDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;


@Service
public class PhoneService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String TWILIO_AUTH_TOKEN;

    @Value("${TWILIO_VERIFICATION_SID}")
    private String TWILIO_VERIFICATION_SID;

    private final PhoneRepository phoneRepository;

    private final UserService userService;


    @Autowired
    public PhoneService(PhoneRepository phoneRepository, UserService userService) {
        this.phoneRepository = phoneRepository;
        this.userService = userService;
    }


    public boolean verifyUserOTP(User user, String otpCode) throws Exception {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        try {

            String phoneNumber = user.getPhones().getFirst().getPhoneNumber();


            VerificationCheck verificationCheck = VerificationCheck.creator(TWILIO_VERIFICATION_SID)
                    .setTo("+1" + phoneNumber).setCode(otpCode).create();

            return verificationCheck.getStatus().equals("approved") ? true : false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public String generateUserOTP(Long userId) {
        User user = this.userService.getUserById(userId);
        String phoneNumber = user.getPhones().getFirst().getPhoneNumber();

        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        Verification verification =
                Verification.creator(TWILIO_VERIFICATION_SID, "+1" + phoneNumber, "sms").create();

        return null;
    }


    private Phone getPhoneById(Long phoneId) {

        return this.phoneRepository.findById(phoneId).orElseThrow(() -> new NotFoundException(
                String.format("A phone with the id %d was not found", phoneId)));

    }

    private boolean phoneAlreadyExists(Long userId) {
        return this.phoneRepository.existsByUserId(userId);
    }

    public void createPhone(Long userId, String phoneNumber) {

        try {

            if (phoneAlreadyExists(userId)) {
                throw new BadRequestException("Phone has already been added to our system");
            }
            Boolean isVerified = false;
            User user = this.userService.getCurrentlyLoggedInUser();

            if (user.getId() != userId) {
                throw new ForbiddenException("Cannot alter settings that are not yours");
            }

            Phone phone = new Phone(phoneNumber, isVerified, user);


            this.phoneRepository.save(phone);

        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
    }

    public PhoneDto getPhone(Long userId) {

        try {

            return this.phoneRepository.getLatestPhoneByUserId(userId);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public void deletePhone(Long phoneId) {
        try {
            Phone phone = getPhoneById(phoneId);

            User user = this.userService.getCurrentlyLoggedInUser();

            if (phone.getUser().getId() != user.getId()) {
                throw new ForbiddenException("Cannot delete another user's phone number");
            }

            this.phoneRepository.deleteByPhoneId(phoneId);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
        }
    }
}
