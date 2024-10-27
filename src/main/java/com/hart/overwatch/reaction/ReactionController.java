package com.hart.overwatch.reaction;

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
import com.hart.overwatch.reaction.request.CreateReactionRequest;
import com.hart.overwatch.reaction.response.CreateReactionResponse;
import com.hart.overwatch.reaction.response.DeleteReactionResponse;
import com.hart.overwatch.reaction.response.GetReactionResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping(path = "/comments/{commentId}/reactions")
    public ResponseEntity<CreateReactionResponse> createReaction(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CreateReactionRequest request) {
        reactionService.createReaction(request, commentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateReactionResponse("success"));
    }

    @GetMapping(path = "/comments/{commentId}/reactions")
    public ResponseEntity<GetReactionResponse> getReaction(
            @PathVariable("commentId") Long commentId, @RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetReactionResponse("success", reactionService.getReaction(commentId, userId)));
    }

    @DeleteMapping(path = "/comments/{commentId}/reactions/{userId}")
    public ResponseEntity<DeleteReactionResponse> deleteReaction(
            @PathVariable("commentId") Long commentId, @PathVariable("userId") Long userId) {
        reactionService.deleteReaction(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteReactionResponse("success"));
    }
}
