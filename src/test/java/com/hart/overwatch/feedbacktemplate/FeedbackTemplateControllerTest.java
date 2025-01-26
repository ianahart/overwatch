package com.hart.overwatch.feedbacktemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.request.CreateFeedbackTemplateRequest;
import com.hart.overwatch.github.dto.GitHubPaginationDto;
import com.hart.overwatch.github.dto.GitHubRepositoryDto;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = FeedbackTemplateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FeedbackTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackTemplateService feedbackTemplateService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private List<FeedbackTemplate> feedbackTemplates = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }


    private List<FeedbackTemplate> createFeedbackTemplates(User user) {
        List<FeedbackTemplate> feedbackTemplateEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            FeedbackTemplate feedbackTemplateEntity = new FeedbackTemplate();
            feedbackTemplateEntity.setId(Long.valueOf(i + 1));
            feedbackTemplateEntity.setUser(user);
            feedbackTemplateEntity.setFeedback("feedback text");
            feedbackTemplateEntities.add(feedbackTemplateEntity);
        }
        return feedbackTemplateEntities;
    }

    private List<MinFeedbackTemplateDto> convertToDtos(List<FeedbackTemplate> feedbackTemplates) {
        return feedbackTemplates.stream()
                .map(v -> new MinFeedbackTemplateDto(v.getId(), v.getUser().getId()))
                .collect(Collectors.toList());
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        feedbackTemplates = createFeedbackTemplates(user);
    }


    @Test
    public void FeedbackTemplateController_CreateFeedbackTemplate_ReturnCreateFeedbackTemplateResponse()
            throws Exception {
        CreateFeedbackTemplateRequest request = new CreateFeedbackTemplateRequest();
        request.setUserId(user.getId());
        request.setFeedback("new feedback");

        doNothing().when(feedbackTemplateService)
                .createFeedbackTemplate(any(CreateFeedbackTemplateRequest.class));

        ResultActions response = mockMvc
                .perform(post("/api/v1/feedback-templates").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

        verify(feedbackTemplateService, times(1))
                .createFeedbackTemplate(any(CreateFeedbackTemplateRequest.class));
    }


}


