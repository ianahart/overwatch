package com.hart.overwatch.commentvote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.commentvote.request.CreateCommentVoteRequest;
import com.hart.overwatch.commentvote.response.CreateCommentVoteResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class CommentVoteController {

    private final CommentVoteService commentVoteService;

    @Autowired
    public CommentVoteController(CommentVoteService commentVoteService) {
        this.commentVoteService = commentVoteService;
    }

    @PostMapping("/comments/{commentId}/votes")
    ResponseEntity<CreateCommentVoteResponse> createCommentVote(
            @Valid @RequestBody CreateCommentVoteRequest request) {
        commentVoteService.createCommentVote(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateCommentVoteResponse("success"));
    }
}
