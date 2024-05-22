package com.hart.overwatch.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.setting.dto.SettingDto;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;


@Service
public class SettingService {

    private final SettingRepository settingRepository;
    private final UserService userService;

    @Autowired
    public SettingService(SettingRepository settingRepository, UserService userService) {
        this.settingRepository = settingRepository;
        this.userService = userService;
    }

    public Setting createSetting() {
        Setting setting = new Setting();

        this.settingRepository.save(setting);

        return setting;
    }


    public Setting getSettingById(Long settingId) {

        try {
            return this.settingRepository.findById(settingId)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("A setting with the id of %d was not found", settingId)));

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public SettingDto getSetting(Long settingId) {
        try {

            return this.settingRepository.fetchSettingById(settingId);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }



    public Boolean updateSettingMfaEnabled(Long settingId, Boolean mfaEnabled) {

        try {
            User currentUser = this.userService.getCurrentlyLoggedInUser();
            Setting currentUserSetting = getSettingById(settingId);

            if (currentUser.getSetting().getId() != currentUserSetting.getId()) {
                throw new ForbiddenException("Cannot update another user's setting");
            }


            currentUserSetting.setMfaEnabled(mfaEnabled);

            this.settingRepository.save(currentUserSetting);

            return currentUserSetting.getMfaEnabled();

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

