package com.hart.overwatch.activity;

import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.activity.request.CreateActivityRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.RateLimitException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final TodoCardService todoCardService;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, TodoCardService todoCardService,
            UserService userService, PaginationService paginationService) {
        this.activityRepository = activityRepository;
        this.todoCardService = todoCardService;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private Activity getActivityById(Long activityId) {
        return activityRepository.findById(activityId).orElseThrow(() -> new NotFoundException(
                String.format("Cannot find activity with the id %d", activityId)));
    }

    private ActivityDto convertToDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setUserId(activity.getUser().getId());
        activityDto.setTodoCardId(activity.getTodoCard().getId());
        activityDto.setCreatedAt(activity.getCreatedAt());
        activityDto.setText(activity.getText());
        activityDto.setAvatarUrl(activity.getUser().getProfile().getAvatarUrl());

        return activityDto;
    }

    public ActivityDto createActivity(String text, Long todoCardId, Long userId) {
        User user = userService.getUserById(userId);
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);

        Activity activity = new Activity(text, user, todoCard);

        activityRepository.save(activity);

        return convertToDto(activity);
    }

    public ActivityDto handleCreateActivity(CreateActivityRequest request) {
        Long userId = request.getUserId();
        Long todoCardId = request.getTodoCardId();
        String text = Jsoup.clean(request.getText(), Safelist.none());

        if (userId == null || todoCardId == null) {
            throw new BadRequestException("Missing either userId or todoCardId parameter");
        }

        int recentActivityCount = activityRepository.countActivitiesByUserIdAndCreatedAtAfter(
                userId, LocalDateTime.now().minusMinutes(10));

        if (recentActivityCount >= 20) {
            throw new RateLimitException(
                    "Rate Limit exceeded: You can only create up to 20 activities in 10 minutes");
        }

        return createActivity(text, todoCardId, userId);
    }

    public PaginationDto<ActivityDto> getActivities(Long todoCardId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<ActivityDto> result =
                this.activityRepository.getAllActivitiesByTodoCardId(todoCardId, pageable);


        return new PaginationDto<ActivityDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

    public void deleteActivity(Long activityId) {
        Activity activity = getActivityById(activityId);
        User user = userService.getCurrentlyLoggedInUser();

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot delete an activity that is not yours");
        }

        int recentActivityCount = activityRepository.countActivitiesByUserIdAndCreatedAtAfter(
                user.getId(), LocalDateTime.now().minusMinutes(10));

        if (recentActivityCount >= 20) {
            throw new RateLimitException(
                    "Rate Limit exceeded: You can only create up to 20 activities in 10 minutes");
        }


        activityRepository.delete(activity);
    }

}
