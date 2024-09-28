package com.hart.overwatch.checklistitem;

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
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.checklist.CheckListService;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;
import com.hart.overwatch.checklistitem.request.CreateCheckListItemRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CheckListItemServiceTest {

    @InjectMocks
    private CheckListItemService checkListItemService;

    @Mock
    CheckListService checkListService;

    @Mock
    UserService userService;

    private User user;

    private WorkSpace workSpace;

    private TodoCard todoCard;

    private CheckList checkList;

    private List<CheckListItem> checkListItems;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        checkList = generateCheckList(user, todoCard);

        checkListItems = generateCheckListItems(user, checkList);
    }

    private List<CheckListItem> generateCheckListItems(User user, CheckList checkList) {
        List<CheckListItem> checkListItems = new ArrayList<>();
        int toGenerate = 2;

        for (int i = 1; i <= toGenerate; i++) {
            CheckListItem checkListItem = new CheckListItem();
            checkListItem.setId(Long.valueOf(i));
            checkListItem.setUser(user);
            checkListItem.setTitle(String.format("checklistitem-%d", i));
            checkListItem.setCheckList(checkList);
            checkListItem.setIsCompleted(false);
            checkListItems.add(checkListItem);
        }
        return checkListItems;
    }

    private CheckList generateCheckList(User user, TodoCard todoCard) {
        LocalDateTime timestamp = LocalDateTime.now();
        CheckList checkList = new CheckList();
        checkList.setId(1L);
        checkList.setUser(user);
        checkList.setTodoCard(todoCard);
        checkList.setIsCompleted(false);
        checkList.setTitle(String.format("checklist-1"));
        checkList.setCreatedAt(timestamp);
        checkList.setUpdatedAt(timestamp);
        return checkList;
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
    public void CheckListItemService_CreateCheckListItem_ThrowBadRequestExceptionMissingParams() {
        CreateCheckListItemRequest request = new CreateCheckListItemRequest();
        request.setCheckListId(null);
        request.setUserId(user.getId());
        request.setTitle("checklistitem-3");


        Assertions.assertThatThrownBy(() -> {
            checkListItemService.createCheckListItem(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Could not complete your request. Please contact support.");
    }

}


