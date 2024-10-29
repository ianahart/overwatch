package com.hart.overwatch.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.setting.dto.SettingDto;
import com.hart.overwatch.setting.request.UpdateSettingRequest;
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
        setting.setRequestPendingNotifOn(true);
        setting.setRequestAcceptedNotifOn(true);
        setting.setReviewCompletedNotifOn(true);
        setting.setReviewInCompleteNotifOn(true);
        setting.setReviewInProgressNotifOn(true);
        setting.setPaymentAcknowledgementNotifOn(true);
        setting.setCommentReplyOn(true);

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

            if (!currentUser.getSetting().getId().equals(currentUserSetting.getId())) {
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

    public SettingDto updateSetting(UpdateSettingRequest request, Long settingId) {
        try {
            User currentUser = this.userService.getCurrentlyLoggedInUser();
            Setting currentUserSetting = getSettingById(settingId);

            if (!currentUser.getId().equals(currentUserSetting.getUser().getId())) {
                throw new ForbiddenException("Cannot update another user's setting");
            }

            currentUserSetting.setUser(currentUser);
            currentUserSetting.setMfaEnabled(request.getSetting().getMfaEnabled());
            currentUserSetting
                    .setRequestPendingNotifOn(request.getSetting().getRequestPendingNotifOn());
            currentUserSetting
                    .setRequestAcceptedNotifOn(request.getSetting().getRequestAcceptedNotifOn());
            currentUserSetting
                    .setReviewCompletedNotifOn(request.getSetting().getReviewCompletedNotifOn());
            currentUserSetting
                    .setReviewInCompleteNotifOn(request.getSetting().getReviewInCompleteNotifOn());
            currentUserSetting
                    .setReviewInProgressNotifOn(request.getSetting().getReviewInProgressNotifOn());
            currentUserSetting.setPaymentAcknowledgementNotifOn(
                    request.getSetting().getPaymentAcknowledgementNotifOn());
            currentUserSetting.setCommentReplyOn(request.getSetting().getCommentReplyOn());


            this.settingRepository.save(currentUserSetting);
            return convertToDto(currentUserSetting);

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private SettingDto convertToDto(Setting setting) {
        SettingDto settingDto = new SettingDto();
        settingDto.setId(setting.getId());
        settingDto.setUserId(setting.getUser().getId());
        settingDto.setMfaEnabled(setting.getMfaEnabled());
        settingDto.setCreatedAt(setting.getCreatedAt());
        settingDto.setReviewInProgressNotifOn(setting.getReviewInProgressNotifOn());
        settingDto.setReviewInCompleteNotifOn(setting.getReviewInCompleteNotifOn());
        settingDto.setReviewCompletedNotifOn(setting.getReviewCompletedNotifOn());
        settingDto.setPaymentAcknowledgementNotifOn(setting.getPaymentAcknowledgementNotifOn());
        settingDto.setRequestPendingNotifOn(setting.getRequestPendingNotifOn());
        settingDto.setRequestAcceptedNotifOn(setting.getRequestAcceptedNotifOn());
        settingDto.setCommentReplyOn(setting.getCommentReplyOn());

        return settingDto;
    }
}

