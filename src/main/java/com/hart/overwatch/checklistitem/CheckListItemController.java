package com.hart.overwatch.checklistitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.checklistitem.request.CreateCheckListItemRequest;
import com.hart.overwatch.checklistitem.request.UpdateCheckListItemRequest;
import com.hart.overwatch.checklistitem.response.CreateCheckListItemResponse;
import com.hart.overwatch.checklistitem.response.DeleteCheckListItemResponse;
import com.hart.overwatch.checklistitem.response.UpdateCheckListItemResponse;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateCheckListItemResponse(
                "success", checkListItemService.createCheckListItem(request)));

    }

    @PutMapping("/checklist-items/{checkListItemId}")
    ResponseEntity<UpdateCheckListItemResponse> updateCheckListItem(
            @Valid @RequestBody UpdateCheckListItemRequest request) {
        checkListItemService.updateCheckListItem(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateCheckListItemResponse("success"));
    }

    @DeleteMapping("/checklist-items/{checkListItemId}")
    ResponseEntity<DeleteCheckListItemResponse> deleteCheckListItem(
            @PathVariable("checkListItemId") Long checkListItemId) {
        checkListItemService.deleteCheckListItem(checkListItemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteCheckListItemResponse("success"));
    }
}
