package com.hart.overwatch.checklistitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.checklistitem.request.CreateCheckListItemRequest;
import com.hart.overwatch.checklistitem.response.CreateCheckListItemResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class CheckListItemController {

    private final CheckListItemService checkListItemService;

    @Autowired
    public CheckListItemController(CheckListItemService checkListItemService) {
        this.checkListItemService = checkListItemService;
    }


    @PostMapping("/checklist-items")
    ResponseEntity<CreateCheckListItemResponse> createCheckListItem(
            @Valid @RequestBody CreateCheckListItemRequest request) {
        checkListItemService.createCheckListItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateCheckListItemResponse("success"));

    }
}
