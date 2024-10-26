package com.hart.overwatch.reportcomment;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class ReportCommentService {


    private final ReportCommentRepository reportCommentRepository;

    private final UserService userService;

    private final CommentService commentService;

    @Autowired
    public ReportCommentService(ReportCommentRepository reportCommentRepository,
            UserService userService, CommentService commentService) {
        this.reportCommentRepository = reportCommentRepository;
        this.userService = userService;
        this.commentService = commentService;
    }


    private Boolean alreadyReportedComment(Long commentId, Long userId) {
        return reportCommentRepository.findReportCommentByCommentIdAndUserId(commentId, userId);
    }

    public void createReportComment(CreateReportCommentRequest request) {
        Long userId = request.getUserId();
        Long commentId = request.getCommentId();
        String details = Jsoup.clean(request.getDetails(), Safelist.none());

        if (alreadyReportedComment(commentId, userId)) {
            throw new BadRequestException("You have already reported this comment");
        }

        User reportedBy = userService.getUserById(userId);
        Comment comment = commentService.getCommentById(commentId);

        ReportComment reportComment =
                new ReportComment(request.getReason(), details, comment, reportedBy);

        reportCommentRepository.save(reportComment);
    }
}

