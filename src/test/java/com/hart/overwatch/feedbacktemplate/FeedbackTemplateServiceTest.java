package com.hart.overwatch.feedbacktemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.feedbacktemplate.request.CreateFeedbackTemplateRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FeedbackTemplateServiceTest {


    @InjectMocks
    private FeedbackTemplateService feedbackTemplateService;

    @Mock
    private FeedbackTemplateRepository feedbackTemplateRepository;

    @Mock
    private UserService userService;

    private final int TEMPLATE_LIMIT = 5;

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


    @BeforeEach
    public void setUp() {
        user = createUser();
        feedbackTemplates = createFeedbackTemplates(user);
    }

    @Test
    public void FeedbackTemplateService_CreateFeedbackTemplate_ThrowBadRequestException() {
        CreateFeedbackTemplateRequest request = new CreateFeedbackTemplateRequest();
        request.setUserId(user.getId());
        request.setFeedback("feedback text");

        when(feedbackTemplateRepository.countFeedbackTemplatesByUserId(user.getId()))
                .thenReturn(TEMPLATE_LIMIT + 1);

        Assertions.assertThatThrownBy(() -> {
            feedbackTemplateService.createFeedbackTemplate(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage(String.format("You cannot exceed %d template limit", TEMPLATE_LIMIT));
    }

    @Test
    public void FeedbackTemplateService_CreateFeedbackTemplate_ReturnNothing() {
        CreateFeedbackTemplateRequest request = new CreateFeedbackTemplateRequest();
        request.setUserId(user.getId());
        request.setFeedback("new feedback");

        when(feedbackTemplateRepository.countFeedbackTemplatesByUserId(user.getId()))
                .thenReturn(TEMPLATE_LIMIT - 1);
        when(userService.getUserById(request.getUserId())).thenReturn(user);

        FeedbackTemplate newFeedbackTemplate = new FeedbackTemplate(request.getFeedback(), user);

        when(feedbackTemplateRepository.save(any(FeedbackTemplate.class)))
                .thenReturn(newFeedbackTemplate);

        feedbackTemplateService.createFeedbackTemplate(request);

        Assertions.assertThatNoException();
        verify(feedbackTemplateRepository, times(1)).save(any(FeedbackTemplate.class));
    }

    @Test
    public void FeedbackTemplateService_GetFeedbackTemplate_ThrowBadRequestException() {
        Long feedbackTemplateId = null;

        Assertions.assertThatThrownBy(() -> {
            feedbackTemplateService.getFeedbackTemplate(feedbackTemplateId);
        }).isInstanceOf(BadRequestException.class).hasMessage("Missing feedback template id");
    }

}


