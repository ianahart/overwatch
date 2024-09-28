package com.hart.overwatch.checklistitem;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.checklist.CheckListService;
import com.hart.overwatch.checklistitem.dto.CheckListItemDto;
import com.hart.overwatch.checklistitem.request.CreateCheckListItemRequest;
import com.hart.overwatch.checklistitem.request.UpdateCheckListItemRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class CheckListItemService {

    private final int ITEMS_PER_LIST = 10;

    private final CheckListItemRepository checkListItemRepository;

    private final CheckListService checkListService;

    private final UserService userService;

    @Autowired
    public CheckListItemService(CheckListItemRepository checkListItemRepository,
            CheckListService checkListService, UserService userService) {
        this.checkListItemRepository = checkListItemRepository;
        this.checkListService = checkListService;
        this.userService = userService;
    }


    private CheckListItem getCheckListItemById(Long checkListItemId) {
        return checkListItemRepository.findById(checkListItemId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Cannot find check list item with id %d", checkListItemId)));
    }


    private long countCheckListItems(Long checkListId) {
        return checkListItemRepository.countCheckListItemsInCheckList(checkListId);
    }

    private boolean checkListItemExists(Long checkListId, Long userId, String title) {
        return checkListItemRepository.checkListItemExistsByUserIdAndCheckListIdAndTitle(userId,

                checkListId, title);
    }

    public CheckListItemDto createCheckListItem(CreateCheckListItemRequest request) {
        Long checkListId = request.getCheckListId();
        Long userId = request.getUserId();
        String title = Jsoup.clean(request.getTitle(), Safelist.none());

        if (checkListId == null || userId == null) {
            throw new BadRequestException(
                    "Could not complete your request. Please contact support.");
        }
        if (countCheckListItems(checkListId) >= ITEMS_PER_LIST) {
            throw new BadRequestException(String.format(
                    "You have reached the maximum amount of list items (%d)", ITEMS_PER_LIST));
        }

        if (checkListItemExists(checkListId, userId, title)) {
            throw new BadRequestException(
                    String.format("You have already added %s in this list", title));
        }

        CheckList checkList = checkListService.getCheckListById(checkListId);
        User user = userService.getUserById(userId);
        Boolean isCompleted = false;

        CheckListItem checkListItem = new CheckListItem(title, isCompleted, user, checkList);

        checkListItemRepository.save(checkListItem);

        return new CheckListItemDto(checkListItem.getId(), checkListItem.getUser().getId(),
                checkListItem.getCheckList().getId(), checkListItem.getTitle(),
                checkListItem.getIsCompleted());
    }

    public void updateCheckListItem(UpdateCheckListItemRequest request) {
        String title = Jsoup.clean(request.getTitle(), Safelist.none());



        CheckListItem checkListItem = getCheckListItemById(request.getId());
        if (!title.toLowerCase().equals(checkListItem.getTitle())) {
            checkListItem.setTitle(title);
        }
        checkListItem.setIsCompleted(request.getIsCompleted());

        checkListItemRepository.save(checkListItem);
    }

    public void deleteCheckListItem(Long checkListItemId) {
        CheckListItem checkListItem = getCheckListItemById(checkListItemId);
        User user = userService.getCurrentlyLoggedInUser();

        if (!checkListItem.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot delete a check list item that is not yours");
        }

        checkListItemRepository.delete(checkListItem);
    }
}
