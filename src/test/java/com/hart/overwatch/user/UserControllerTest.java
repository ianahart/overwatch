package com.hart.overwatch.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.dto.UpdateUserDto;
import com.hart.overwatch.user.request.DeleteUserRequest;
import com.hart.overwatch.user.request.UpdateUserPasswordRequest;
import com.hart.overwatch.user.request.UpdateUserRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        Setting setting = new Setting();
        setting.setId(1L);
        profile.setId(1L);
        profile.setAvatarUrl("http://example.com/avatar.jpg");
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", setting);

        user.setId(1L);

    }

    @Test
    public void UserController_UpdateUserPassword_ReturnUpdateUserPasswordResponse()
            throws Exception {
        String currentPassword = "Test12345%";
        String newPassword = "Test12345%%";
        UpdateUserPasswordRequest request =
                new UpdateUserPasswordRequest(currentPassword, newPassword);

        doNothing().when(userService).updateUserPassword(currentPassword, newPassword,
                user.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/v1/users/1/password").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void UserController_UpdateUser_ReturnUpdateUserResponse() throws Exception {

        String firstName = "john";
        String lastName = "smith";
        String email = "smith@mail.com";

        UpdateUserRequest request = new UpdateUserRequest(firstName, lastName, email);
        UpdateUserDto updateUserDto = new UpdateUserDto("John", "Smith", email, "JS");

        when(userService.updateUser(request, user.getId())).thenReturn(updateUserDto);

        ResultActions response =
                mockMvc.perform(patch("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_DeleteUser_ReturnDeleteUserResponse() throws Exception {

        DeleteUserRequest deleteUserRequest = new DeleteUserRequest("Test12345%");

        doNothing().when(userService).deleteUser(user.getId(), deleteUserRequest.getPassword());

        ResultActions response = mockMvc
                .perform(post("/api/v1/users/1/delete").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andDo(MockMvcResultHandlers.print());

    }

}
