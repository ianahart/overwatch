package com.hart.overwatch.comment;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final TopicService topicService;

    private final PaginationService paginationService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService,
            TopicService topicService, PaginationService paginationService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.paginationService = paginationService;
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find comment with the id %d", commentId)));
    }


    public void createComment(CreateCommentRequest request) {
        User user = userService.getUserById(request.getUserId());
        Topic topic = topicService.getTopicById(request.getTopicId());
        String content = Jsoup.clean(request.getContent(), Safelist.none());

        Comment comment = new Comment(content, user, topic);

        commentRepository.save(comment);
    }

    public PaginationDto<CommentDto> getComments(Long topicId, int page, int pageSize,
            String direction, String sort) {

        Pageable pageable;
        if (sort.toLowerCase().equals("desc") || sort.toLowerCase().equals("asc")) {
            pageable = paginationService.getSortedPageable(page, pageSize, direction, sort);
        } else {
            pageable = paginationService.getPageable(page, pageSize, direction);
        }

        Page<CommentDto> result;

        if (sort.toLowerCase().equals("vote")) {
            result = this.commentRepository.getCommentsByTopicIdWithVoteDifference(topicId,
                    pageable);
        } else {
            result = this.commentRepository.getCommentsByTopicId(topicId, pageable);
        }


        return new PaginationDto<CommentDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }

}
