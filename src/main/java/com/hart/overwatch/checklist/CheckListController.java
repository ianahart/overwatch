package com.hart.overwatch.checklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;
import com.hart.overwatch.checklist.response.CreateCheckListResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class CheckListController {

    private final CheckListService checkListService;

    @Autowired
    public CheckListController(CheckListService checkListService) {
        this.checkListService = checkListService;
    }

    @PostMapping("/checklists")
    ResponseEntity<CreateCheckListResponse> createCheckList(
            @Valid @RequestBody CreateCheckListRequest request) {
        checkListService.createCheckList(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateCheckListResponse("success"));
    }
}
