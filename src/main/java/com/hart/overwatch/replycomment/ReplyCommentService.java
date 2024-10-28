package com.hart.overwatch.replycomment;

import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.replycomment.request.CreateReplyCommentRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.RateLimitException;

@Service
public class ReplyCommentService {

    private final int REPLY_LIMIT = 20;

    private final ReplyCommentRepository replyCommentRepository;

    private final UserService userService;

    private final CommentService commentService;

    private final PaginationService paginationService;

    @Autowired
    public ReplyCommentService(ReplyCommentRepository replyCommentRepository,
            UserService userService, CommentService commentService,
            PaginationService paginationService) {
        this.replyCommentRepository = replyCommentRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.paginationService = paginationService;
    }

    private ReplyComment getReplyCommentById(Long replyCommentId) {
        return replyCommentRepository.findById(replyCommentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Could not find a reply comment with the id %d")));
    }



    private boolean canPostReply(Long userId) {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        int replyCommentCount =
                replyCommentRepository.countByUserIdAndCreatedAtAfter(userId, fiveMinutesAgo);
        return replyCommentCount < REPLY_LIMIT;
    }

    public void createReplyComment(CreateReplyCommentRequest request, Long commentId) {
        Long userId = request.getUserId();
        String content = Jsoup.clean(request.getContent(), Safelist.none());

        if (!canPostReply(userId)) {
            throw new RateLimitException(
                    "You have exceed the amount of comments in a single timespan. Please wait before posting again");
        }

        User user = userService.getUserById(userId);
        Comment comment = commentService.getCommentById(commentId);

        ReplyComment replyComment = new ReplyComment(false, content, user, comment);

        replyCommentRepository.save(replyComment);
    }
}
