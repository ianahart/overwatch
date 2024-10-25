package com.hart.overwatch.commentvote;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.commentvote.request.CreateCommentVoteRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class CommentVoteService {

    private final CommentVoteRepository commentVoteRepository;

    private final CommentService commentService;

    private final UserService userService;

    @Autowired
    public CommentVoteService(CommentVoteRepository commentVoteRepository,
            CommentService commentService, UserService userService) {
        this.commentVoteRepository = commentVoteRepository;
        this.commentService = commentService;
        this.userService = userService;
    }

    private CommentVote getCommentVoteById(Long commentVoteId) {
        return commentVoteRepository.findById(commentVoteId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Comment vote with the id %d was not found", commentVoteId)));
    }


    private boolean commentVoteAlreadyExists(Long commentId, Long userId) {
        if (commentId != null && userId != null) {
            return commentVoteRepository.findByCommentIdAndUserId(commentId, userId);
        }
        return true;
    }

    public void createCommentVote(CreateCommentVoteRequest request) {
        Long userId = request.getUserId();
        Long commentId = request.getCommentId();
        String voteType = Jsoup.clean(request.getVoteType(), Safelist.none());

        if (commentVoteAlreadyExists(commentId, userId)) {
            throw new BadRequestException("You have already voted on this comment");
        }

        User user = userService.getUserById(userId);
        Comment comment = commentService.getCommentById(commentId);

        CommentVote commentVote = new CommentVote(comment, user, voteType);

        commentVoteRepository.save(commentVote);
    }
}
