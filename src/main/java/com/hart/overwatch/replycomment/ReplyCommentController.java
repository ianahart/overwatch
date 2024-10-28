package com.hart.overwatch.replycomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.replycomment.request.CreateReplyCommentRequest;
import com.hart.overwatch.replycomment.response.CreateReplyCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/comments")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @Autowired
    public ReplyCommentController(ReplyCommentService replyCommentService) {
        this.replyCommentService = replyCommentService;
    }

    @PostMapping(path = "/{commentId}/reply")
    public ResponseEntity<CreateReplyCommentResponse> createReplyComment(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CreateReplyCommentRequest request) {
        replyCommentService.createReplyComment(request, commentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateReplyCommentResponse("success"));
    }
}
