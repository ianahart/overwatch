package com.hart.overwatch.reportcomment;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.reportcomment.dto.ReportCommentDto;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class ReportCommentService {


    private final ReportCommentRepository reportCommentRepository;

    private final UserService userService;

    private final CommentService commentService;

    private final PaginationService paginationService;

    @Autowired
    public ReportCommentService(ReportCommentRepository reportCommentRepository,
            UserService userService, CommentService commentService,
            PaginationService paginationService) {
        this.reportCommentRepository = reportCommentRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.paginationService = paginationService;
    }


    private ReportComment getReportCommentById(Long reportCommentId) {
        return reportCommentRepository.findById(reportCommentId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find report comment with the id %d", reportCommentId)));
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

        ReportComment reportComment = new ReportComment(ReportStatus.ACTIVE, request.getReason(),
                details, comment, reportedBy);

        reportCommentRepository.save(reportComment);
    }

    public PaginationDto<ReportCommentDto> getReportComments(int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<ReportCommentDto> result = reportCommentRepository.getReportComments(pageable);

        return new PaginationDto<ReportCommentDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

    @Transactional
    public void deleteReportComment(Long reportCommentId) {
        User user = userService.getCurrentlyLoggedInUser();

        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have the ability to delete reported comments");
        }

        ReportComment reportComment = getReportCommentById(reportCommentId);

        Long commentId = reportComment.getComment().getId();
        commentService.deleteComment(commentId);

        reportComment.setStatus(ReportStatus.DELETED);
        reportCommentRepository.save(reportComment);
    }

}

