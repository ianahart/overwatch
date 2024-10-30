package com.hart.overwatch.savecomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class SaveCommentService {

    private final SaveCommentRepository saveCommentRepository;

    private final UserService userService;

    private final CommentService commentService;

    private final PaginationService paginationService;

    @Autowired
    public SaveCommentService(SaveCommentRepository saveCommentRepository, UserService userService,
            CommentService commentService, PaginationService paginationService) {
        this.saveCommentRepository = saveCommentRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.paginationService = paginationService;
    }

    private SaveComment getSaveCommentById(Long saveCommentId) {
        return saveCommentRepository.findById(saveCommentId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find a save comment with the id %d", saveCommentId)));
    }


    private Boolean alreadySavedComment(Long commentId, Long userId) {
        return saveCommentRepository.findSaveCommentByUserIdAndCommentId(userId, commentId);
    }

    public void createSaveComment(CreateSaveCommentRequest request) {

        Long userId = request.getUserId();
        Long commentId = request.getCommentId();

        if (alreadySavedComment(commentId, userId)) {
            throw new BadRequestException("You have already saved this comment");
        }

        User user = userService.getUserById(userId);
        Comment comment = commentService.getCommentById(commentId);

        SaveComment saveComment = new SaveComment(user, comment);

        saveCommentRepository.save(saveComment);
    }

    public PaginationDto<SaveCommentDto> getSaveComments(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable =
                this.paginationService.getSortedPageable(page, pageSize, direction, "desc");
        Page<SaveCommentDto> result =
                this.saveCommentRepository.findSaveCommentsByUserId(pageable, userId);

        return new PaginationDto<SaveCommentDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }

    public void DeleteSaveComment(Long saveCommentId) {
        SaveComment saveComment = getSaveCommentById(saveCommentId);
        User user = userService.getCurrentlyLoggedInUser();

        if (!user.getId().equals(saveComment.getUser().getId())) {
            throw new ForbiddenException("You cannot unsave a comment that is not yours");
        }

        saveCommentRepository.delete(saveComment);
    }

}
