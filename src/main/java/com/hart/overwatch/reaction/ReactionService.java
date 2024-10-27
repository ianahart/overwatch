package com.hart.overwatch.reaction;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.reaction.request.CreateReactionRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;

    private final UserService userService;

    private final CommentService commentService;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository, UserService userService,
            CommentService commentService) {
        this.reactionRepository = reactionRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    private Reaction getReactionById(Long reactionId) {
        return reactionRepository.findById(reactionId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find reaction with id %d", reactionId)));
    }


    private Boolean alreadyReactedWithEmoji(Long userId, Long commentId) {
        return reactionRepository.existsByUserIdAndCommentId(userId, commentId);
    }

    public void createReaction(CreateReactionRequest request, Long commentId) {
        Long userId = request.getUserId();
        String emoji = Jsoup.clean(request.getEmoji(), Safelist.none());

        if (alreadyReactedWithEmoji(userId, commentId)) {
            throw new BadRequestException(String.format("You already reacted with %s", emoji));
        }

        User user = userService.getUserById(userId);
        Comment comment = commentService.getCommentById(commentId);

        Reaction reaction = new Reaction(emoji, user, comment);

        reactionRepository.save(reaction);
    }

    public String getReaction(Long commentId, Long userId) {
        if (commentId == null || userId == null) {
            throw new BadRequestException("Missing either comment id or user id");
        }

        Reaction reaction = reactionRepository.findByCommentIdAndUserId(commentId, userId);

        return reaction == null ? null : reaction.getEmoji();
    }

    public void deleteReaction(Long commentId, Long userId) {
        if (commentId == null || userId == null) {
            throw new BadRequestException("Missing either comment id or user id");
        }

        Reaction reaction = reactionRepository.findByCommentIdAndUserId(commentId, userId);
        User user = userService.getCurrentlyLoggedInUser();

        if (reaction != null && user.getId().equals(reaction.getUser().getId())) {
            reactionRepository.delete(reaction);
        }
    }
}
