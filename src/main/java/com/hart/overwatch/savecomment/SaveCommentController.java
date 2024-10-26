package com.hart.overwatch.savecomment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.savecomment.response.CreateSaveCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/save-comments")
public class SaveCommentController {

    private final SaveCommentService saveCommentService;

    public SaveCommentController(SaveCommentService saveCommentService) {
        this.saveCommentService = saveCommentService;
    }

    @PostMapping("")
    public ResponseEntity<CreateSaveCommentResponse> createSaveComment(
            @Valid @RequestBody CreateSaveCommentRequest request) {
        saveCommentService.createSaveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateSaveCommentResponse("success"));
    }
}
