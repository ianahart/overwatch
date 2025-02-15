package com.hart.overwatch.setting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.sql.Timestamp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.dto.SettingDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SettingServiceTest {

    @InjectMocks
    private SettingService settingService;

    @Mock
    private SettingRepository settingRepository;


    @Mock
    private UserService userService;

    private User user;

    private Setting setting;

    @BeforeEach
    void setUp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        setting = new Setting();
        setting.setId(1L);
        setting.setCreatedAt(timestamp);
        setting.setUpdatedAt(timestamp);
        setting.setMfaEnabled(false);

        user.setId(1L);
        user.setSetting(setting);
        setting.setUser(user);
    }

    @Test
    public void SettingService_CreateSetting_ReturnSetting() {
        Setting settingToBeSaved = new Setting();
        settingToBeSaved.setId(2L);
        when(settingRepository.save(any(Setting.class))).thenReturn(settingToBeSaved);

        Setting savedSetting = settingService.createSetting();

        Assertions.assertThat(savedSetting).isNotNull();
    }

    @Test
    public void SettingService_GetSettingById_ReturnSetting() {
        when(settingRepository.findById(setting.getId())).thenReturn(Optional.ofNullable(setting));

        Setting returnedSetting = settingService.getSettingById(setting.getId());

        Assertions.assertThat(returnedSetting).isNotNull();
        Assertions.assertThat(returnedSetting.getId()).isEqualTo(setting.getId());
    }

    @Test
    public void SettingService_GetSettingById_ThrowNotFoundException() {
        Long nonExistentSettingId = 999L;
        when(settingRepository.findById(nonExistentSettingId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> settingService.getSettingById(nonExistentSettingId))
                .isInstanceOf(NotFoundException.class).hasMessage(String
                        .format("A setting with the id of %d was not found", nonExistentSettingId));

    }

    @Test
    public void SettingService_GetSetting_ReturnSettingDto() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Boolean mfaEnabled = false;
        SettingDto settingDto = new SettingDto(1L, user.getId(), mfaEnabled, timestamp, true, true,
                true, true, true, true, true, true);

        when(settingRepository.findById(setting.getId())).thenReturn(Optional.of(setting));
        when(settingRepository.fetchSettingById(1L)).thenReturn(settingDto);


        SettingDto returnedSettingDto = settingService.getSetting(1L);

        Assertions.assertThat(returnedSettingDto).isNotNull();
        Assertions.assertThat(returnedSettingDto.getId()).isEqualTo(settingDto.getId());
    }

    @Test
    public void SettingService_UpdateSettingMfaEnabled_ReturnBooleanMfaEnabled() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(settingRepository.findById(setting.getId())).thenReturn(Optional.of(setting));
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        Boolean mfaEnabledToUpdate = true;
        Boolean mfaEnabled = settingService.updateSettingMfaEnabled(setting.getId(), mfaEnabledToUpdate);

        Assertions.assertThat(mfaEnabled).isTrue();
    }

    @Test
    public void SettingService_UpdateSettingMfaEnabled_ThrowForbiddenException() {
        User unauthorizedUser = new User();
        unauthorizedUser.setId(2L);
        Setting unauthorizedUserSetting = new Setting();
        unauthorizedUserSetting.setId(2L);
        unauthorizedUser.setSetting(unauthorizedUserSetting);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(unauthorizedUser);
        when(settingRepository.findById(setting.getId())).thenReturn(Optional.of(setting));

        Boolean mfaEnabledToUpdate = true;
        Assertions
                .assertThatThrownBy(() -> settingService.updateSettingMfaEnabled(setting.getId(),
                        mfaEnabledToUpdate))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot update another user's setting");
    }

    @Test
    public void SettingService_UnsubscribeFromEmail_ThrowBadRequestException() {
        String email = null;

        Assertions.assertThatThrownBy(() -> {
            settingService.unsubscribeFromEmail(email);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Unsuccessful in unsubscribing due to invalid email");
    }

    @Test
    public void SettingService_UnsubscribeFromEmail_ReturnNothing() {
        String email = user.getEmail();

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(settingRepository.findById(user.getSetting().getId()))
                .thenReturn(Optional.of(setting));
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        settingService.unsubscribeFromEmail(email);

        verify(settingRepository, times(1)).save(any(Setting.class));
    }
}

