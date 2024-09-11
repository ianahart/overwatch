package com.hart.overwatch.checklist;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.checklist.dto.CheckListDto;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;

@Service
public class CheckListService {

    private final int CHECKLISTS_PER_CARD = 5;

    private final CheckListRepository checkListRepository;

    private final UserService userService;

    private final TodoCardService todoCardService;


    @Autowired
    public CheckListService(CheckListRepository checkListRepository, UserService userService,
            TodoCardService todoCardService) {
        this.checkListRepository = checkListRepository;
        this.userService = userService;
        this.todoCardService = todoCardService;
    }

    private CheckList getCheckListById(Long checkListId) {
        return checkListRepository.findById(checkListId).orElseThrow(() -> new NotFoundException(
                String.format("CheckList with id %d was not found", checkListId)));
    }

    private long countCheckListsInCard(Long todoCardId) {
        return checkListRepository.countCheckListsInTodoCard(todoCardId);
    }

    private boolean checkListInCard(Long todoCardId, Long userId, String title) {
        return checkListRepository.checkListExistsByTitleAndUserIdAndTodoCardId(userId, todoCardId,
                title);
    }

    public void createCheckList(CreateCheckListRequest request) {
        Long todoCardId = request.gettodoCardId();
        Long userId = request.getUserId();
        String title = Jsoup.clean(request.getTitle(), Safelist.none());

        if (todoCardId == null || userId == null) {
            throw new BadRequestException("Missing todoCardId or userId parameter");
        }

        if (countCheckListsInCard(todoCardId) >= CHECKLISTS_PER_CARD) {
            throw new BadRequestException(String.format(
                    "You have added the maximum (%d) amount of checklists for this card",
                    CHECKLISTS_PER_CARD));
        }

        if (checkListInCard(todoCardId, userId, title)) {
            throw new BadRequestException("You have already added a checklist with that title");
        }

        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);
        User user = userService.getUserById(userId);

        CheckList checkList = new CheckList();
        checkList.setTitle(title);
        checkList.setIsCompleted(false);
        checkList.setUser(user);
        checkList.setTodoCard(todoCard);

        checkListRepository.save(checkList);
    }

    public List<CheckListDto> getCheckLists(Long todoCardId) {
        if (todoCardId == null) {
            throw new BadRequestException("Missing todoCardId parameter");
        }

        Pageable pageable =
                PageRequest.of(0, CHECKLISTS_PER_CARD, Sort.by("createdAt").descending());

        Page<CheckListDto> result =
                checkListRepository.getCheckListsByTodoCardId(todoCardId, pageable);

        return result.getContent();
    }

    public void deleteCheckList(Long checkListId) {
        if (checkListId == null) {
            throw new BadRequestException("Missing checkListId parameter");
        }

        User user = userService.getCurrentlyLoggedInUser();
        CheckList checkList = getCheckListById(checkListId);

        if (user.getId() != checkList.getUser().getId()) {
            throw new ForbiddenException("Cannot delete a checklist that is not yours");
        }

        checkListRepository.delete(checkList);
    }
}
