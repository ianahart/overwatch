package com.hart.overwatch.replycomment;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.replycomment.request.CreateReplyCommentRequest;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
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

}


