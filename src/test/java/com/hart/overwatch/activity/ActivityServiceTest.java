package com.hart.overwatch.activity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.Collections;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.activity.request.CreateActivityRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.RateLimitException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @InjectMocks
    private ActivityService activityService;


    @Mock
    ActivityRepository activityRepository;

    @Mock
    TodoCardService todoCardService;

    @Mock
    PaginationService paginationService;

    @Mock
    private UserService userService;

    private User user;

    private WorkSpace workSpace;

    private TodoList todoList;

    private TodoCard todoCard;

    private Activity activity;

    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        user.setId(1L);

        workSpace = new WorkSpace("work space title", "#0000FF", user);
        workSpace.setId(1L);
        todoList = new TodoList(user, workSpace, "todo list title", 0);
        todoList.setId(1L);
        todoCard = new TodoCard("todo card title", 0, user, todoList);
        todoCard.setId(1L);

        activity = new Activity("some activity text", user, todoCard);

        activity.setId(1L);
    }

    @Test
    public void ActivityService_HandleCreateActivity_ThrowBadRequestExceptionMissingParams() {
        CreateActivityRequest request = new CreateActivityRequest(null, null, "activity text");

        Assertions.assertThatThrownBy(() -> {
            activityService.handleCreateActivity(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing either userId or todoCardId parameter");
    }

    @Test
    public void ActivityService_HandleCreateActivity_ThrowRateLimitException() {
        CreateActivityRequest request =
                new CreateActivityRequest(todoCard.getId(), user.getId(), "activity text");
        int MAX_RECENT_ACTIVITY_COUNT = 20;

        when(activityRepository.countActivitiesByUserIdAndCreatedAtAfter(anyLong(),
                any(LocalDateTime.class))).thenReturn(MAX_RECENT_ACTIVITY_COUNT + 1);

        Assertions.assertThatThrownBy(() -> {
            activityService.handleCreateActivity(request);
        }).isInstanceOf(RateLimitException.class).hasMessage(
                "Rate Limit exceeded: You can only create up to 20 activities in 10 minutes");

    }

    @Test
    public void ActivityService_HandleCreateActivity_ReturnActivityDto() {
        CreateActivityRequest request =
                new CreateActivityRequest(todoCard.getId(), user.getId(), "some activity text");

        when(activityRepository.countActivitiesByUserIdAndCreatedAtAfter(anyLong(),
                any(LocalDateTime.class))).thenReturn(1);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(todoCardService.getTodoCardById(todoCard.getId())).thenReturn(todoCard);

        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        ActivityDto activityDto = activityService.handleCreateActivity(request);

        Assertions.assertThat(activityDto).isNotNull();
        verify(activityRepository, times(1)).save(any(Activity.class));
        Assertions.assertThat(activityDto.getText()).isEqualTo("some activity text");
    }

    @Test
    public void ActivityService_GetActivities_ReturnPaginationDtoOfActivityDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        ActivityDto activityDto = new ActivityDto();
        Page<ActivityDto> pageResult =
                new PageImpl<>(Collections.singletonList(activityDto), pageable, 1);
        PaginationDto<ActivityDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(activityRepository.getAllActivitiesByTodoCardId(todoCard.getId(), pageable))
                .thenReturn(pageResult);

        PaginationDto<ActivityDto> actualPaginationDto =
                activityService.getActivities(todoCard.getId(), page, pageSize, direction);
        Assertions.assertThat(actualPaginationDto.getItems())
                .isEqualTo(expectedPaginationDto.getItems());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
    }

    public void ActivityService_DeleteActivity_ThrowForbiddenException() {
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            activityService.deleteActivity(activity.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot delete an activity that is not yours");
    }

    public void ActivityService_DeleteActivity_ReturnNothing() {
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(activityRepository.countActivitiesByUserIdAndCreatedAtAfter(anyLong(), any(LocalDateTime.class))).thenReturn(1);

        doNothing().when(activityRepository).delete(activity);

        activityService.deleteActivity(activity.getId());

        verify(activityRepository, times(1)).delete(activity);
    }

}


