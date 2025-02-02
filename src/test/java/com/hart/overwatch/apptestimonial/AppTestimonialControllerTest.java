package com.hart.overwatch.apptestimonial;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;
import com.hart.overwatch.apptestimonial.dto.MinAppTestimonialDto;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = AppTestimonialController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AppTestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppTestimonialService appTestimonialService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private AppTestimonial appTestimonial;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        profile.setId(1L);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private AppTestimonial createAppTestimonial(User user) {
        AppTestimonial appTestimonialEntity = new AppTestimonial();
        appTestimonialEntity.setId(1L);
        appTestimonialEntity.setDeveloperType("Frontend Developer");
        appTestimonialEntity.setContent("testimonial content");
        appTestimonialEntity.setUser(user);
        appTestimonialEntity.setIsSelected(false);

        return appTestimonialEntity;
    }

    private AppTestimonialDto convertToDto(AppTestimonial appTestimonial) {
        AppTestimonialDto appTestimonialDto = new AppTestimonialDto();
        appTestimonialDto.setId(appTestimonial.getId());
        appTestimonialDto.setContent(appTestimonial.getContent());
        appTestimonialDto.setDeveloperType(appTestimonial.getDeveloperType());
        appTestimonialDto.setFirstName(appTestimonial.getUser().getFirstName());
        appTestimonialDto.setAvatarUrl(appTestimonial.getUser().getProfile().getAvatarUrl());

        return appTestimonialDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        appTestimonial = createAppTestimonial(user);
    }

    @Test
    public void AppTestimonialController_CreateAppTestimonial_ReturnCreateAppTestimonialResponse()
            throws Exception {
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        request.setUserId(user.getId());
        request.setContent("new content");
        request.setDeveloperType("Frontend Developer");

        doNothing().when(appTestimonialService).createAppTestimonial(request);
        ResultActions response = mockMvc
                .perform(post("/api/v1/app-testimonials").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void AppTestimonialController_GetAppTestimonial_ReturnGetSingleAppTestimonialResponse()
            throws Exception {

        MinAppTestimonialDto minAppTestimonialDto = new MinAppTestimonialDto();
        minAppTestimonialDto.setId(appTestimonial.getId());
        minAppTestimonialDto.setContent(appTestimonial.getContent());
        minAppTestimonialDto.setDeveloperType(appTestimonial.getDeveloperType());
        when(appTestimonialService.getAppTestimonial()).thenReturn(minAppTestimonialDto);

        ResultActions response = mockMvc.perform((get("/api/v1/app-testimonials/single")));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(minAppTestimonialDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content",
                        CoreMatchers.is(minAppTestimonialDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.developerType",
                        CoreMatchers.is(minAppTestimonialDto.getDeveloperType())));
    }

    @Test
    public void AppTestimonialController_UpdateAppTestimonial_ReturnUpdateAppTestimonialResponse()
            throws Exception {
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        request.setUserId(user.getId());
        request.setContent("updated content");
        request.setDeveloperType("Backend Develpoer");

        doNothing().when(appTestimonialService).updateAppTestimonial(request,
                appTestimonial.getId());
        ResultActions response = mockMvc
                .perform(patch(String.format("/api/v1/app-testimonials/%d", appTestimonial.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void AppTestimonialController_DeleteAppTestimonial_ReturnDeleteAppTestimonialResponse()
            throws Exception {
        Long appTestimonialId = appTestimonial.getId();

        doNothing().when(appTestimonialService).deleteAppTestimonial(appTestimonialId);

        ResultActions response = mockMvc.perform(
                delete(String.format("/api/v1/app-testimonials/%d", appTestimonial.getId())));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void AppTestimonialController_GetAppTestimonials_ReturnGetAllAppTestimonialsResponse()
            throws Exception {
        Integer pageSize = 2;
        List<AppTestimonialDto> appTestimonialDtos = List.of(convertToDto(appTestimonial));

        when(appTestimonialService.getAppTestimonials(pageSize)).thenReturn(appTestimonialDtos);

        ResultActions response =
                mockMvc.perform(get("/api/v1/app-testimonials", appTestimonial.getId())
                        .param("pageSize", pageSize.toString()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(appTestimonial.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content",
                        CoreMatchers.is(appTestimonial.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].developerType",
                        CoreMatchers.is(appTestimonial.getDeveloperType())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].avatarUrl",
                        CoreMatchers.is(appTestimonial.getUser().getProfile().getAvatarUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(appTestimonial.getUser().getFirstName())));
    }

}


