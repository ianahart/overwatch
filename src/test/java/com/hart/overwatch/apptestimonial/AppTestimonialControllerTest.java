package com.hart.overwatch.apptestimonial;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.topic.Topic;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.hamcrest.CoreMatchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = AppTestimonialController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AppTestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppTestimonialService appTestimonialService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
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



    // @Test
    // public void
    // ReportCommentControllerTest_CreateReportComment_ReturnCreateReportCommentResponse()
    // throws Exception {
    // CreateReportCommentRequest request = new CreateReportCommentRequest();
    // Comment newComment = new Comment();
    // newComment.setId(2L);
    // newComment.setContent("some content");
    //
    // request.setUserId(user.getId());
    // request.setCommentId(newComment.getId());
    // request.setDetails("details");
    // request.setReason(ReportReason.MISINFORMATION);
    //
    // doNothing().when(reportCommentService).createReportComment(request);
    //
    // ResultActions response = mockMvc
    // .perform(post("/api/v1/report-comments").contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(request)));
    // response.andExpect(MockMvcResultMatchers.status().isCreated())
    // .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    //
    // }

}


