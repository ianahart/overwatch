package com.hart.overwatch.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.dto.SettingDto;
import com.hart.overwatch.setting.request.UpdateSettingMfaEnabledRequest;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import java.sql.Timestamp;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = SettingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SettingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingService settingService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Setting setting;



    @BeforeEach
    public void init() {
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
    public void SettingController_GetSetting_ReturnGetSettingResponse() throws Exception {
        SettingDto settingDto = new SettingDto(setting.getId(), user.getId(),
                setting.getMfaEnabled(), setting.getCreatedAt());

        when(this.settingService.getSetting(setting.getId())).thenReturn(settingDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/settings/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(setting.getId().intValue())));

    }

    @Test
    public void SettingController_UpdateSetting_ReturnUpdateSettingMfaEnabledResponse()
            throws Exception {
        Boolean mfaEnabled = true;

        UpdateSettingMfaEnabledRequest request = new UpdateSettingMfaEnabledRequest(mfaEnabled);

        when(settingService.updateSettingMfaEnabled(setting.getId(), request.getMfaEnabled()))
                .thenReturn(true);

        ResultActions response = mockMvc.perform(patch("/api/v1/settings/1/mfa-enabled")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mfaEnabled", CoreMatchers.is(true)));

    }

}


