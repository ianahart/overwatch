package com.hart.overwatch.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.comment.response.CreateCommentResponse;
import com.hart.overwatch.comment.response.GetAllCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(path = "/topics/{topicId}/comments")
    public ResponseEntity<GetAllCommentResponse> getComments(@PathVariable("topicId") Long topicId,
            @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
            @RequestParam("direction") String direction, @RequestParam("sort") String sort) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllCommentResponse("success",
                commentService.getComments(topicId, page, pageSize, direction, sort)));
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @Valid @RequestBody CreateCommentRequest request) {
        commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateCommentResponse("success"));
    }
}
