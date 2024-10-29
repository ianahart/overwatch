package com.hart.overwatch.comment;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.comment.dto.MinCommentDto;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.comment.request.UpdateCommentRequest;
import com.hart.overwatch.commentvote.CommentVote;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

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
        Boolean isEdited = false;

        Comment comment = new Comment(content, isEdited, user, topic);

        commentRepository.save(comment);
    }

    private List<CommentDto> attachExtraFields(List<CommentDto> commentDtos, User user) {
        for (CommentDto commentDto : commentDtos) {
            Comment comment = getCommentById(commentDto.getId());
            if (user == null) {
                commentDto.setCurUserVoteType(null);
                commentDto.setCurUserHasVoted(false);
                commentDto.setCurUserHasSaved(false);
            } else {
                CommentVote commentVote = comment.getCommentVotes().stream()
                        .filter(vote -> vote.getUser().getId().equals(user.getId())
                                && vote.getVoteType() != null)
                        .findFirst().orElse(null);

                commentDto.setCurUserHasSaved(comment.getSavedComments().stream().anyMatch(
                        saveComment -> saveComment.getUser().getId().equals(user.getId())));

                if (commentVote != null) {
                    commentDto.setCurUserHasVoted(true);
                    commentDto.setCurUserVoteType(commentVote.getVoteType());
                }
            }

            commentDto.setReactions(comment.getGroupReactionsByComment());
        }
        return commentDtos;
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

        User currentUser = userService.getCurrentlyLoggedInUser();

        List<CommentDto> comments = attachExtraFields(result.getContent(), currentUser);
        return new PaginationDto<CommentDto>(comments, result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }

    public void updateComment(UpdateCommentRequest request, Long commentId) {
        if (commentId == null) {
            throw new BadRequestException("Missing commentId from request. Please try again");
        }

        User user = userService.getCurrentlyLoggedInUser();
        Comment comment = getCommentById(commentId);

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new ForbiddenException("Cannot update a comment that is not yours");
        }

        String content = Jsoup.clean(request.getContent(), Safelist.none());

        comment.setContent(content);
        comment.setIsEdited(true);

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        if (commentId == null) {
            throw new BadRequestException("Missing commentId from request. Please try again");
        }

        User user = userService.getCurrentlyLoggedInUser();
        Comment comment = getCommentById(commentId);

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new ForbiddenException("Cannot update a comment that is not yours");
        }
        commentRepository.delete(comment);
    }


    private MinCommentDto convertToMinDto(Comment comment) {
        MinCommentDto commentDto = new MinCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setContent(comment.getContent());
        commentDto.setFullName(comment.getUser().getFullName());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setAvatarUrl(comment.getUser().getProfile().getAvatarUrl());
        return commentDto;
    }

    public MinCommentDto getComment(Long commentId) {
        Comment comment = getCommentById(commentId);

        return convertToMinDto(comment);
    }
}
