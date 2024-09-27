package com.hart.overwatch.checklist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
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
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.label.LabelService;
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
public class CheckListServiceTest {

    @InjectMocks
    private CheckListService checkListService;

    @Mock
    CheckListRepository checkListRepository;

    @Mock
    TodoCardService todoCardService;

    @Mock
    UserService userService;

    private User user;

    private WorkSpace workSpace;

    private TodoCard todoCard;

    private List<CheckList> checkLists;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        checkLists = generateCheckLists(user, todoCard);
    }

    private List<CheckList> generateCheckLists(User user, TodoCard todoCard) {
        LocalDateTime timestamp = LocalDateTime.now();
        int toGenerate = 2;
        List<CheckList> checkLists = new ArrayList<>();

        for (int i = 1; i <= toGenerate; i++) {
            CheckList checkList = new CheckList();
            checkList.setId(Long.valueOf(i));
            checkList.setUser(user);
            checkList.setTodoCard(todoCard);
            checkList.setIsCompleted(false);
            checkList.setTitle(String.format("checklist-%d", i));
            checkList.setCreatedAt(timestamp);
            checkList.setUpdatedAt(timestamp);

            checkLists.add(checkList);
        }
        return checkLists;
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

    @Test
    public void CheckListService_GetCheckListById_ThrowNotFoundException() {
        CheckList checkList = checkLists.getFirst();

        when(checkListRepository.findById(checkList.getId())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            checkListService.getCheckListById(checkList.getId());
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("CheckList with id %d was not found", checkList.getId()));
    }

    @Test
    public void CheckListService_GetCheckListById_ReturnCheckList() {
        CheckList checkList = checkLists.getFirst();

        when(checkListRepository.findById(checkList.getId())).thenReturn(Optional.of(checkList));

        CheckList returnedCheckList = checkListService.getCheckListById(checkList.getId());

        Assertions.assertThat(returnedCheckList).isNotNull();
        Assertions.assertThat(returnedCheckList.getId()).isEqualTo(checkList.getId());
    }

    @Test
    public void CheckListService_CreateCheckList_ThrowBadRequestExceptionMissingParams() {
        CreateCheckListRequest request = new CreateCheckListRequest();
        request.setUserId(user.getId());
        request.setTitle("checklist-3");
        request.settodoCardId(null);


        Assertions.assertThatThrownBy(() -> {
            checkListService.createCheckList(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing todoCardId or userId parameter");
    }

}


