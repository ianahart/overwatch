package com.hart.overwatch.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.label.request.CreateLabelRequest;
import com.hart.overwatch.label.request.UpdateLabelRequest;
import com.hart.overwatch.label.response.CreateLabelResponse;
import com.hart.overwatch.label.response.DeleteLabelResponse;
import com.hart.overwatch.label.response.GetLabelsResponse;
import com.hart.overwatch.label.response.UpdateLabelResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/labels")
public class LabelController {


    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping("")
    public ResponseEntity<CreateLabelResponse> createLabel(
            @Valid @RequestBody CreateLabelRequest request) {
        labelService.createLabel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateLabelResponse("success"));
    }

    @GetMapping("")
    public ResponseEntity<GetLabelsResponse> getLabels(
            @RequestParam(name = "workSpaceId", required = true) Long workSpaceId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetLabelsResponse("success", labelService.getLabels(workSpaceId)));
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<DeleteLabelResponse> deleteLabel(@PathVariable("labelId") Long labelId) {

        labelService.deleteLabel(labelId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteLabelResponse("success"));
    }

    @PatchMapping("/{labelId}")
    public ResponseEntity<UpdateLabelResponse> updateLabel(@PathVariable("labelId") Long labelId, @RequestBody UpdateLabelRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateLabelResponse("success", labelService.updateLabel(labelId, request)));
    }
}
