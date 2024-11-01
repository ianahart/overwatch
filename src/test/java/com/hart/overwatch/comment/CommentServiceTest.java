package com.hart.overwatch.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.comment.request.UpdateCommentRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reaction.dto.ReactionDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    TopicService topicService;

    @Mock
    private UserService userService;


    @Mock
    private PaginationService paginationService;


    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("https://imgur.com/profile-pic");
        profileEntity.setId(1L);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);;
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicEntity.setId(1L);
        return topicEntity;
    }

    private List<Tag> createTags() {
        List<Tag> tagEntities = new ArrayList<>();
        tagEntities.add(new Tag(1L, "spring boot"));
        tagEntities.add(new Tag(2L, "java"));
        return tagEntities;
    }


    private List<Comment> createComments(User user, Topic topic) {
        int count = 3;
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Comment comment = new Comment();
            comment.setId(Long.valueOf(i + 1));
            comment.setTopic(topic);
            comment.setUser(user);
            comment.setContent(String.format("content-%d", i + 1));
            comment.setIsEdited(false);
            comment.setCommentVotes(new ArrayList<>());
            comment.setSavedComments(new ArrayList<>());
            comment.setReactions(new ArrayList<>());
            comments.add(comment);
        }
        return comments;
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setContent(comment.getContent());
        commentDto.setFullName(comment.getUser().getFullName());
        commentDto.setIsEdited(comment.getIsEdited());
        commentDto.setAvatarUrl(comment.getUser().getProfile().getAvatarUrl());
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setVoteDifference(2L);
        commentDto.setReactions(List.of(new ReactionDto()));
        commentDto.setCurUserVoteType("UPVOTE");
        commentDto.setCurUserHasSaved(true);
        commentDto.setCurUserHasVoted(true);
        commentDto.setReplyCommentsCount(0);

        return commentDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        topic = createTopic(user);
        tags = createTags();
        comments = createComments(user, topic);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }


    @Test
    public void CommentService_GetCommentById_ThrowNotFoundException() {
        Long nonExistentCommentId = 999L;

        when(commentRepository.findById(nonExistentCommentId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            commentService.getCommentById(nonExistentCommentId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("Could not find comment with the id %d", nonExistentCommentId));
    }

    @Test
    public void CommentService_GetCommentById_ReturnComment() {
        Comment comment = comments.get(0);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Comment returnedComment = commentService.getCommentById(comment.getId());

        Assertions.assertThat(returnedComment).isNotNull();
        Assertions.assertThat(returnedComment.getId()).isEqualTo(comment.getId());
        Assertions.assertThat(returnedComment.getIsEdited()).isEqualTo(comment.getIsEdited());
        Assertions.assertThat(returnedComment.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    public void CommentService_CreateComment_ReturnNothing() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setUserId(user.getId());
        request.setTopicId(topic.getId());
        request.setContent("new comment here");

        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(topicService.getTopicById(request.getTopicId())).thenReturn(topic);
        String content = Jsoup.clean(request.getContent(), Safelist.none());
        Boolean isEdited = false;

        Comment newComment = new Comment(content, isEdited, user, topic);

        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        commentService.createComment(request);

        verify(commentRepository, times(1)).save(any(Comment.class));

        Assertions.assertThatNoException();
    }

    @Test
    public void CommentService_GetCommentsWithVoteDifference_ReturnPaginationOfCommentDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String sort = "vote";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<CommentDto> commentDtos =
                comments.stream().map(this::convertToDto).collect(Collectors.toList());
        Page<CommentDto> pageResult = new PageImpl<>(commentDtos, pageable, commentDtos.size());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(commentRepository.getCommentsByTopicIdWithVoteDifference(1L, pageable))
                .thenReturn(pageResult);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        for (Comment comment : comments) {
            when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        }

        PaginationDto<CommentDto> actualPaginationDto =
                commentService.getComments(1L, page, pageSize, direction, sort);

        Assertions.assertThat(actualPaginationDto).isNotNull();
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(commentDtos.size());
        CommentDto resultDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(resultDto.getCurUserVoteType()).isEqualTo("UPVOTE");
        Assertions.assertThat(resultDto.getCurUserHasVoted()).isTrue();
        Assertions.assertThat(resultDto.getCurUserHasSaved()).isFalse();
        Assertions.assertThat(resultDto.getReplyCommentsCount()).isEqualTo(0);
    }

    @Test
    public void CommentService_GetComments_ReturnPaginationOfCommentDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String sort = "asc";

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        List<CommentDto> commentDtos =
                comments.stream().map(this::convertToDto).collect(Collectors.toList());
        Page<CommentDto> pageResult = new PageImpl<>(commentDtos, pageable, commentDtos.size());

        when(paginationService.getSortedPageable(page, pageSize, direction, sort))
                .thenReturn(pageable);
        when(commentRepository.getCommentsByTopicId(eq(1L), any(Pageable.class)))
                .thenReturn(pageResult);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        for (Comment comment : comments) {
            when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        }

        PaginationDto<CommentDto> actualPaginationDto =
                commentService.getComments(1L, page, pageSize, direction, sort);

        Assertions.assertThat(actualPaginationDto).isNotNull();
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(commentDtos.size());
        CommentDto resultDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(resultDto.getCurUserVoteType()).isEqualTo("UPVOTE");
        Assertions.assertThat(resultDto.getCurUserHasVoted()).isTrue();
        Assertions.assertThat(resultDto.getCurUserHasSaved()).isFalse();
        Assertions.assertThat(resultDto.getReplyCommentsCount()).isEqualTo(0);
    }

    @Test
    public void CommentService_UpdateComment_ThrowBadRequestException() {
        Long commentId = null;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUserId(user.getId());
        request.setContent("updated-content");

        Assertions.assertThatThrownBy(() -> {
            commentService.updateComment(request, commentId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing commentId from request. Please try again");
    }

    @Test
    public void CommentService_UpdateComment_ThrowForbiddenException() {
        Comment comment = comments.get(0);
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUserId(user.getId());
        request.setContent("updated-content");

        User forbiddenUser = new User();
        forbiddenUser.setId(999L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Assertions.assertThatThrownBy(() -> {
            commentService.updateComment(request, comment.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot update a comment that is not yours");
    }

    @Test
    public void CommentService_UpdateComment_ReturnNothing() {
        Comment comment = comments.get(0);
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUserId(user.getId());
        request.setContent("updated-content");

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        comment.setContent(Jsoup.clean(request.getContent(), Safelist.none()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.updateComment(request, comment.getId());

        verify(commentRepository, times(1)).save(any(Comment.class));

        Assertions.assertThatNoException();
    }

    @Test
    public void CommentService_DeleteComment_ThrowBadRequestException() {
        Long commentId = null;

        Assertions.assertThatThrownBy(() -> {
            commentService.deleteComment(commentId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing commentId from request. Please try again");
    }

    @Test
    public void CommentService_DeleteComment_ThrowForbiddenException() {
        Comment comment = comments.get(0);
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Assertions.assertThatThrownBy(() -> {
            commentService.deleteComment(comment.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot delete a comment that is not yours");
    }

    @Test
    public void CommentService_DeleteComment_ReturnNothing() {
        Comment comment = comments.get(0);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(comment.getId());

        verify(commentRepository, times(1)).delete(comment);

        Assertions.assertThatNoException();
    }
}


