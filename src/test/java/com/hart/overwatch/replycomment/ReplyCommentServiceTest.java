package com.hart.overwatch.replycomment;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.RateLimitException;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.replycomment.dto.ReplyCommentDto;
import com.hart.overwatch.replycomment.request.CreateReplyCommentRequest;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReplyCommentServiceTest {

    @InjectMocks
    private ReplyCommentService replyCommentService;

    @Mock
    ReplyCommentRepository replyCommentRepository;

    @Mock
    private PaginationService paginationService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    private User user;

    private Comment comment;

    private ReplyComment replyComment;



    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicEntity.setId(1L);
        return topicEntity;
    }

    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setUser(user);
        commentEntity.setTopic(topic);
        commentEntity.setContent("content");
        commentEntity.setIsEdited(false);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setReactions(new ArrayList<>());
        commentEntity.setCommentVotes(new ArrayList<>());
        commentEntity.setId(1L);
        return commentEntity;
    }


    private ReplyComment createReplyComment(User user, Comment comment) {
        LocalDateTime createdAt = LocalDateTime.now();
        ReplyComment replyCommentEntity = new ReplyComment();
        replyCommentEntity.setIsEdited(false);
        replyCommentEntity.setContent("content");
        replyCommentEntity.setUser(user);
        replyCommentEntity.setComment(comment);
        replyCommentEntity.setCreatedAt(createdAt);
        replyCommentEntity.setId(1L);

        return replyCommentEntity;

    }

    private ReplyCommentDto convertToDto(ReplyComment replyComment) {
        ReplyCommentDto replyCommentDto = new ReplyCommentDto();
        replyCommentDto.setId(replyComment.getId());
        replyCommentDto.setUserId(replyComment.getUser().getId());
        replyCommentDto.setContent(replyComment.getContent());
        replyCommentDto.setFullName(replyComment.getUser().getFullName());
        replyCommentDto.setAvatarUrl(replyComment.getUser().getProfile().getAvatarUrl());
        replyCommentDto.setCreatedAt(LocalDateTime.now());

        return replyCommentDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        replyComment = createReplyComment(user, comment);

    }

    @Test
    public void ReplyCommentService_CreateReplyComment_ThrowRateLimitException() {
        int REPLY_LIMIT = 20;
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        Long commentId = comment.getId();
        Long userId = user.getId();
        String content = "content";

        CreateReplyCommentRequest request = new CreateReplyCommentRequest();
        request.setContent(content);
        request.setUserId(userId);

        when(replyCommentRepository.countByUserIdAndCreatedAtAfter(eq(userId),
                argThat(timestamp -> timestamp.isAfter(fiveMinutesAgo.minusSeconds(1))
                        && timestamp.isBefore(fiveMinutesAgo.plusSeconds(1)))))
                                .thenReturn(REPLY_LIMIT);

        Assertions.assertThatThrownBy(() -> {
            replyCommentService.createReplyComment(request, commentId);
        }).isInstanceOf(RateLimitException.class).hasMessage(
                "You have exceed the amount of comments in a single timespan. Please wait before posting again");
    }


    @Test
    public void ReplyCommentService_CreateReplyComment_ReturnNothing() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        Long commentId = comment.getId();
        Long userId = user.getId();
        String content = "content";

        CreateReplyCommentRequest request = new CreateReplyCommentRequest();
        request.setContent(content);
        request.setUserId(userId);

        when(replyCommentRepository
                .countByUserIdAndCreatedAtAfter(eq(request.getUserId()),
                        argThat(timestamp -> timestamp.isAfter(fiveMinutesAgo.minusSeconds(1))
                                && timestamp.isBefore(fiveMinutesAgo.plusSeconds(1)))))
                                        .thenReturn(0);

        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(commentService.getCommentById(commentId)).thenReturn(comment);
        ReplyComment newReplyComment = new ReplyComment(false, request.getContent(), user, comment);
        when(replyCommentRepository.save(any(ReplyComment.class))).thenReturn(newReplyComment);

        replyCommentService.createReplyComment(request, commentId);
        Assertions.assertThatNoException();
        verify(replyCommentRepository, times(1)).save(any(ReplyComment.class));
    }

    @Test
    public void ReplyCommentService_GetReplyComments_ReturnPaginationDtoOfReplyCommentDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<ReplyCommentDto> replyCommentDtos =
                List.of(replyComment).stream().map(this::convertToDto).collect(Collectors.toList());
        Page<ReplyCommentDto> pageResult =
                new PageImpl<>(replyCommentDtos, pageable, replyCommentDtos.size());

        when(paginationService.getSortedPageable(page, pageSize, direction, "desc"))
                .thenReturn(pageable);
        when(replyCommentRepository.findReplyCommentsByCommentId(pageable, comment.getId()))
                .thenReturn(pageResult);

        PaginationDto<ReplyCommentDto> result =
                replyCommentService.getReplyComments(comment.getId(), page, pageSize, direction);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getItems()).hasSize(replyCommentDtos.size());
        ReplyCommentDto replyCommentDto = result.getItems().get(0);
        Assertions.assertThat(replyCommentDto.getUserId())
                .isEqualTo(replyComment.getUser().getId());
        Assertions.assertThat(replyCommentDto.getContent()).isEqualTo(replyComment.getContent());
        Assertions.assertThat(replyCommentDto.getFullName())
                .isEqualTo(replyComment.getUser().getFullName());
        Assertions.assertThat(replyCommentDto.getId()).isEqualTo(replyComment.getId());

    }
}


