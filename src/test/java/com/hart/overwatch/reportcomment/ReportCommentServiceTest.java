package com.hart.overwatch.reportcomment;


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
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.profile.Profile;
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
public class ReportCommentServiceTest {

    @InjectMocks
    private ReportCommentService reportCommentService;

    @Mock
    ReportCommentRepository reportCommentRepository;

    @Mock
    TopicService topicService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;


    private User user;

    private Comment comment;

    private ReportComment reportComment;

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
        commentEntity.setId(1L);
        commentEntity.setUser(user);
        commentEntity.setTopic(topic);
        commentEntity.setContent("content");
        commentEntity.setIsEdited(false);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setReactions(new ArrayList<>());
        commentEntity.setCommentVotes(new ArrayList<>());
        return commentEntity;
    }

    private ReportComment createReportComment(User user, Comment comment) {
        ReportComment reportCommentEntity = new ReportComment();
        reportCommentEntity.setId(1L);
        reportCommentEntity.setReason(ReportReason.MISINFORMATION);
        reportCommentEntity.setDetails("details");
        reportCommentEntity.setComment(comment);
        reportCommentEntity.setReportedBy(user);
        return reportCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        reportComment = createReportComment(user, comment);

    }

    @Test
    public void ReportCommentService_CreateReportComment_ThrowBadRequestException() {
        CreateReportCommentRequest request = new CreateReportCommentRequest();
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());
        request.setDetails("details");
        request.setReason(ReportReason.MISINFORMATION);

        when(reportCommentRepository.findReportCommentByCommentIdAndUserId(request.getCommentId(),
                request.getUserId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            reportCommentService.createReportComment(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already reported this comment");
    }

    @Test
    public void ReportCommentService_CreateReportComment_ReturnNothing() {
        CreateReportCommentRequest request = new CreateReportCommentRequest();
        request.setUserId(user.getId());
        Comment newComment = new Comment();
        newComment.setId(2L);
        newComment.setContent("some content");
        request.setCommentId(newComment.getId());
        request.setDetails("details");
        request.setReason(ReportReason.MISINFORMATION);

        when(reportCommentRepository.findReportCommentByCommentIdAndUserId(request.getCommentId(),
                request.getUserId())).thenReturn(false);

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(commentService.getCommentById(newComment.getId())).thenReturn(newComment);

        ReportComment newReportComment = new ReportComment();
        newReportComment.setId(1L);
        newReportComment.setDetails(request.getDetails());
        newReportComment.setReason(ReportReason.MISINFORMATION);
        newReportComment.setReportedBy(user);
        newReportComment.setComment(newComment);

        when(reportCommentRepository.save(any(ReportComment.class))).thenReturn(newReportComment);

        reportCommentService.createReportComment(request);
    }
}


