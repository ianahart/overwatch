package com.hart.overwatch.phone;

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

    private final PhoneRepository phoneRepository;

    private final UserService userService;


    public PhoneService(PhoneRepository phoneRepository, UserService userService) {
        this.phoneRepository = phoneRepository;
        this.userService = userService;
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
