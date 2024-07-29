package com.hart.overwatch.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.favorite.request.ToggleFavoriteRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@WebMvcTest(controllers = FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private ToggleFavoriteRequest toggleFavoriteRequest;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        user1 = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user2 = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        toggleFavoriteRequest =
                new ToggleFavoriteRequest(user1.getId(), user2.getProfile().getId(), true);
    }

    @Test
    public void FavoriteController_DeleteOrCreateFavorite_ReturnSuccess() throws Exception {
        doNothing().when(favoriteService).toggleFavorite(ArgumentMatchers.anyBoolean(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        ResultActions response =
                mockMvc.perform(post("/api/v1/favorites").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toggleFavoriteRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
