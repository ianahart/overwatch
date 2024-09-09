package com.hart.overwatch.activelabel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.activelabel.request.CreateActiveLabelRequest;
import com.hart.overwatch.activelabel.response.CreateActiveLabelResponse;
import com.hart.overwatch.activelabel.response.DeleteActiveLabelResponse;
import com.hart.overwatch.activelabel.response.GetActiveLabelResponse;

@RestController
@RequestMapping(path = "/api/v1/active-labels")
public class ActiveLabelController {


    private final ActiveLabelService activeLabelService;


    @Autowired
    public ActiveLabelController(ActiveLabelService activeLabelService) {
        this.activeLabelService = activeLabelService;
    }

    @PostMapping("")
    public ResponseEntity<CreateActiveLabelResponse> createActiveLabel(
            @RequestBody CreateActiveLabelRequest request) {
        activeLabelService.createActiveLabel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateActiveLabelResponse("success"));
    }

    @DeleteMapping("/{todoCardId}")
    public ResponseEntity<DeleteActiveLabelResponse> deleteActiveLabel(
            @PathVariable("todoCardId") Long todoCardId, @RequestParam("labelId") Long labelId) {
        activeLabelService.deleteActiveLabel(todoCardId, labelId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteActiveLabelResponse("success"));
    }

    @GetMapping("")
    public ResponseEntity<GetActiveLabelResponse> getActiveLabels(
            @RequestParam(name = "todoCardId", required = true) Long todoCardId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetActiveLabelResponse("success",
                activeLabelService.getActiveLabels(todoCardId)));
    }

}
