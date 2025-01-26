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
import com.hart.overwatch.github.dto.GitHubPaginationDto;
import com.hart.overwatch.github.dto.GitHubRepositoryDto;
import com.hart.overwatch.github.dto.GitHubTreeDto;
import com.hart.overwatch.githubtoken.GitHubTokenService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.util.ReflectionTestUtils;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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



}


