package com.hart.overwatch.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.comment.response.CreateCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @Valid @RequestBody CreateCommentRequest request) {
        commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateCommentResponse("success"));
    }
}
