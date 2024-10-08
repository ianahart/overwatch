package com.hart.overwatch.activelabel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.activelabel.dto.ActiveLabelDto;
import com.hart.overwatch.activelabel.request.CreateActiveLabelRequest;
import com.hart.overwatch.activity.ActivityService;
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.label.LabelService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ActiveLabelServiceTest {

    @InjectMocks
    private ActiveLabelService activeLabelService;

    @Mock
    ActiveLabelRepository activeLabelRepository;

    @Mock
    LabelService labelService;

    @Mock
    ActivityService activityService;

    @Mock
    TodoCardService todoCardService;

    private User user;

    private WorkSpace workSpace;

    private TodoCard todoCard;

    private List<Label> labels;

    private ActiveLabel activeLabel;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        labels = generateLabels(user, workSpace);

        activeLabel = generateActiveLabel(labels.get(0), todoCard);
    }

    private TodoList generateTodoList(User user, WorkSpace workSpace) {
        TodoList todoList = new TodoList();
        todoList.setId(1L);
        todoList.setUser(user);
        todoList.setWorkSpace(workSpace);
        todoList.setTitle("title");
        todoList.setIndex(0);

        return todoList;
    }

    private ActiveLabel generateActiveLabel(Label label, TodoCard todoCard) {
        ActiveLabel activeLabel = new ActiveLabel();
        activeLabel.setId(1L);
        activeLabel.setLabel(label);
        activeLabel.setTodoCard(todoCard);

        return activeLabel;
    }

    private TodoCard generateTodoCard(User user, TodoList todoList) {
        TodoCard todoCard = new TodoCard();
        todoCard.setId(1L);
        todoCard.setUser(user);
        todoCard.setTitle("card title");
        todoCard.setIndex(0);
        todoCard.setTodoList(todoList);

        return todoCard;
    }

    private WorkSpace generatedWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(1L);
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);

        return workSpace;
    }

    private User generateUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setFullName("John Doe");
        user.setEmail("john@mail.com");
        user.setRole(Role.USER);
        user.setLoggedIn(true);
        user.setProfile(new Profile());
        user.setPassword("Test12345%");
        user.setSetting(new Setting());

        return user;
    }

    private List<Label> generateLabels(User user, WorkSpace workSpace) {
        LocalDateTime timestamp = LocalDateTime.now();
        String[] titles = new String[] {"priority", "warning"};
        List<Label> labels = new ArrayList<>();
        int toGenerate = 2;
        for (int i = 1; i <= toGenerate; i++) {
            Label label = new Label();
            label.setId(Long.valueOf(i));
            label.setUser(user);
            label.setWorkSpace(workSpace);
            label.setColor("#000000");
            label.setTitle(titles[i - 1]);
            label.setIsChecked(false);
            label.setCreatedAt(timestamp);
            label.setUpdatedAt(timestamp);
            labels.add(label);
        }
        return labels;
    }

    @Test
    public void ActiveLabelService_CreateActiveLabel_Throw_BadRequestException() {
        CreateActiveLabelRequest request = new CreateActiveLabelRequest();
        Label label = labels.get(0);
        request.setLabelId(label.getId());
        request.setTodoCardId(null);

        Assertions.assertThatThrownBy(() -> {
            activeLabelService.createActiveLabel(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Either todo card id is missing or label id is missing");
    }

    @Test
    public void ActiveLabelService_CreateActiveLabel_ReturnNothing() {
        CreateActiveLabelRequest request = new CreateActiveLabelRequest();
        Label label = labels.get(0);
        request.setLabelId(label.getId());
        request.setTodoCardId(todoCard.getId());

        when(labelService.getLabelById(label.getId())).thenReturn(label);
        when(todoCardService.getTodoCardById(todoCard.getId())).thenReturn(todoCard);

        when(activeLabelRepository.save(any(ActiveLabel.class))).thenReturn(activeLabel);

        String text = String.format("You added the label %s to your card",
                activeLabel.getLabel().getTitle());

        when(activityService.createActivity(text, todoCard.getId(), todoCard.getUser().getId()))
                .thenReturn(new ActivityDto());

        activeLabelService.createActiveLabel(request);

        verify(activeLabelRepository, times(1)).save(any(ActiveLabel.class));
    }

    @Test
    public void ActiveLabelService_DeleteActiveLabel_ReturnNothing() {
        Long todoCardId = todoCard.getId();
        Long labelId = labels.get(0).getId();

        when(activeLabelRepository.findByTodoCardIdAndLabelId(todoCardId, labelId))
                .thenReturn(activeLabel);

        String text = String.format("You removed the label %s to your card",
                activeLabel.getLabel().getTitle());
        when(activityService.createActivity(text, todoCardId,
                activeLabel.getTodoCard().getUser().getId())).thenReturn(new ActivityDto());
        doNothing().when(activeLabelRepository).delete(activeLabel);

        activeLabelService.deleteActiveLabel(todoCardId, labelId);

        verify(activeLabelRepository, times(1)).delete(activeLabel);
    }

    @Test
    public void ActiveLabelService_GetActiveLabels_ReturnListOfActiveLabelDtos() {
        ActiveLabelDto activeLabelDto = new ActiveLabelDto();
        activeLabelDto.setId(activeLabel.getId());
        activeLabelDto.setColor(activeLabel.getLabel().getColor());
        activeLabelDto.setTitle(activeLabel.getLabel().getTitle());
        activeLabelDto.setLabelId(labels.get(0).getId());
        activeLabelDto.setTodoCardId(todoCard.getId());

        when(activeLabelRepository.getActiveLabels(todoCard.getId()))
                .thenReturn(List.of(activeLabelDto));

        List<ActiveLabelDto> activeLabelDtos = activeLabelService.getActiveLabels(todoCard.getId());

        Assertions.assertThat(activeLabelDtos).isNotNull();
        Assertions.assertThat(activeLabelDtos.size()).isEqualTo(1);
    }
}


