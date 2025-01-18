package com.hart.overwatch.feedbacktemplate;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.request.CreateFeedbackTemplateRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class FeedbackTemplateService {

    private final static int TEMPLATE_LIMIT = 5;

    private FeedbackTemplateRepository feedbackTemplateRepository;

    private UserService userService;

    @Autowired
    public FeedbackTemplateService(FeedbackTemplateRepository feedbackTemplateRepository,
            UserService userService) {
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.userService = userService;
    }

    private FeedbackTemplateDto convertToDto(FeedbackTemplate feedbackTemplate) {
        FeedbackTemplateDto dto = new FeedbackTemplateDto();

        dto.setId(feedbackTemplate.getId());
        dto.setUserId(feedbackTemplate.getUser().getId());
        dto.setFeedback(feedbackTemplate.getFeedback());

        return dto;
    }

    private FeedbackTemplate getFeedbackTemplateById(Long feedbackTemplateId) {
        return feedbackTemplateRepository.findById(feedbackTemplateId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Could not find a feedback template with the id %d", feedbackTemplateId)));
    }

    private boolean exceedsTemplateLimit(Long userId) {
        return feedbackTemplateRepository.countFeedbackTemplatesByUserId(userId) >= TEMPLATE_LIMIT;
    }

    public void createFeedbackTemplate(CreateFeedbackTemplateRequest request) {
        Long userId = request.getUserId();
        String feedback = request.getFeedback();

        if (exceedsTemplateLimit(userId)) {
            throw new BadRequestException(
                    String.format("You cannot exceed %d template limit", TEMPLATE_LIMIT));
        }

        User user = userService.getUserById(userId);

        FeedbackTemplate feedbackTemplate = new FeedbackTemplate(feedback, user);

        feedbackTemplateRepository.save(feedbackTemplate);
    }

    public FeedbackTemplateDto getFeedbackTemplate(Long feedbackTemplateId) {
        if (feedbackTemplateId == null) {
            throw new BadRequestException("Missing feedback template id");
        }

        User user = userService.getCurrentlyLoggedInUser();
        FeedbackTemplate feedbackTemplate = getFeedbackTemplateById(feedbackTemplateId);

        if (!user.getId().equals(feedbackTemplate.getUser().getId())) {
            throw new ForbiddenException("Cannot access another user's feedback template");
        }

        return convertToDto(feedbackTemplate);
    }

    public List<MinFeedbackTemplateDto> getFeedbackTemplates() {
        Long userId = userService.getCurrentlyLoggedInUser().getId();

        return feedbackTemplateRepository.getFeedbackTemplates(userId);
    }

    public void deleteFeedbackTemplate(Long feedbackTemplateId) {
        User user = userService.getCurrentlyLoggedInUser();
        FeedbackTemplate feedbackTemplate = getFeedbackTemplateById(feedbackTemplateId);

        if (!user.getId().equals(feedbackTemplate.getUser().getId())) {
            throw new ForbiddenException("Cannot delete another user's feedback template");
        }

        feedbackTemplateRepository.delete(feedbackTemplate);
    }
}
