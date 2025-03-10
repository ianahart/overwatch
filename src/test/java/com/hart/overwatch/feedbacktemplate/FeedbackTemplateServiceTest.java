package com.hart.overwatch.feedbacktemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;
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

    @Test
    public void FeedbackTemplateService_GetFeedbackTemplate_ThrowForbiddenException() {
        FeedbackTemplate feedbackTemplate = feedbackTemplates.get(0);
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(feedbackTemplateRepository.findById(feedbackTemplate.getId()))
                .thenReturn(Optional.of(feedbackTemplate));

        Assertions.assertThatThrownBy(() -> {
            feedbackTemplateService.getFeedbackTemplate(feedbackTemplate.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot access another user's feedback template");
    }

    @Test
    public void FeedbackTemplateService_GetFeedbackTemplate_ReturnFeedbackTemplateDto() {
        FeedbackTemplate feedbackTemplate = feedbackTemplates.get(0);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(feedbackTemplateRepository.findById(feedbackTemplate.getId()))
                .thenReturn(Optional.of(feedbackTemplate));

        FeedbackTemplateDto feedbackTemplateDto =
                feedbackTemplateService.getFeedbackTemplate(feedbackTemplate.getId());

        Assertions.assertThat(feedbackTemplateDto).isNotNull();
        Assertions.assertThat(feedbackTemplateDto.getId()).isEqualTo(feedbackTemplate.getId());
        Assertions.assertThat(feedbackTemplateDto.getUserId())
                .isEqualTo(feedbackTemplate.getUser().getId());
        Assertions.assertThat(feedbackTemplateDto.getFeedback())
                .isEqualTo(feedbackTemplate.getFeedback());
    }

    @Test
    public void FeedbackTemplateService_GetFeedbackTemplates_ReturnListOfMinFeedbackTemplateDtos() {
        Long userId = user.getId();
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        when(feedbackTemplateRepository.getFeedbackTemplates(userId))
                .thenReturn(convertToDtos(feedbackTemplates));

        List<MinFeedbackTemplateDto> minFeedbackTemplateDtos =
                feedbackTemplateService.getFeedbackTemplates();

        Assertions.assertThat(minFeedbackTemplateDtos).hasSize(2);

        for (int i = 0; i < minFeedbackTemplateDtos.size(); i++) {
            Assertions.assertThat(minFeedbackTemplateDtos.get(i).getId())
                    .isEqualTo(feedbackTemplates.get(i).getId());
            Assertions.assertThat(minFeedbackTemplateDtos.get(i).getUserId())
                    .isEqualTo(feedbackTemplates.get(i).getUser().getId());
        }
    }

    @Test
    public void FeedbackTemplateService_DeleteFeedbackTemplate_ThrowForbiddenException() {
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(feedbackTemplateRepository.findById(feedbackTemplates.get(0).getId()))
                .thenReturn(Optional.of(feedbackTemplates.get(0)));

        Assertions.assertThatThrownBy(() -> {
            feedbackTemplateService.deleteFeedbackTemplate(feedbackTemplates.get(0).getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot delete another user's feedback template");
    }

    @Test
    public void FeedbackTemplateService_DeleteFeedbackTemplate_ReturnNothing() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(feedbackTemplateRepository.findById(feedbackTemplates.get(0).getId()))
                .thenReturn(Optional.of(feedbackTemplates.get(0)));

        doNothing().when(feedbackTemplateRepository).delete(feedbackTemplates.get(0));

        feedbackTemplateService.deleteFeedbackTemplate(feedbackTemplates.get(0).getId());
        verify(feedbackTemplateRepository, times(1)).delete(feedbackTemplates.get(0));
    }

}


