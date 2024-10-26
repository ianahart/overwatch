package com.hart.overwatch.savecomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class SaveCommentService {

    private final SaveCommentRepository saveCommentRepository;

    private final UserService userService;

    private final CommentService commentService;

    @Autowired
    public SaveCommentService(SaveCommentRepository saveCommentRepository, UserService userService,
            CommentService commentService) {
        this.saveCommentRepository = saveCommentRepository;
        this.userService = userService;
        this.commentService = commentService;
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
}
