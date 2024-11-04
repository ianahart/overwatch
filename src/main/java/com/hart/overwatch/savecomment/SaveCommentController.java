package com.hart.overwatch.savecomment;

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
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.savecomment.response.CreateSaveCommentResponse;
import com.hart.overwatch.savecomment.response.DeleteSaveCommentResponse;
import com.hart.overwatch.savecomment.response.GetSaveCommentsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/save-comments")
public class SaveCommentController {

    private final SaveCommentService saveCommentService;

    public SaveCommentController(SaveCommentService saveCommentService) {
        this.saveCommentService = saveCommentService;
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateSaveCommentResponse> createSaveComment(
            @Valid @RequestBody CreateSaveCommentRequest request) {
        saveCommentService.createSaveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateSaveCommentResponse("success"));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetSaveCommentsResponse> getSaveComments(
            @RequestParam("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetSaveCommentsResponse("success",
                saveCommentService.getSaveComments(userId, page, pageSize, direction)));
    }

    @DeleteMapping(path = "/{saveCommentId}")
    public ResponseEntity<DeleteSaveCommentResponse> deleteSaveComment(
            @PathVariable("saveCommentId") Long saveCommentId) {
        saveCommentService.deleteSaveComment(saveCommentId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteSaveCommentResponse("success"));
    }
}
