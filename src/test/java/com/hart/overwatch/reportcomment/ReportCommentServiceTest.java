package com.hart.overwatch.reportcomment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
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
import com.hart.overwatch.commentvote.request.CreateCommentVoteRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.topic.Topic;
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
    private CommentService commentService;


    @Mock
    private ReportCommentRepository reportCommentRepository;

    @Mock
    private UserService userService;
    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private ReportComment reportComment;

    private Comment comment;


    private User createUser(String email) {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        profile.setId(1L);

        User userEntity = new User(email, "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        userEntity.setId(1L);
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

    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setId(1L);
        commentEntity.setTopic(topic);
        commentEntity.setUser(user);
        commentEntity.setContent(String.format("content"));
        commentEntity.setIsEdited(false);

        return commentEntity;
    }

    private ReportComment createReportComment(User user, Comment comment) {
        ReportComment reportCommentEntity = new ReportComment();
        reportCommentEntity.setId(1L);
        reportCommentEntity.setDetails("details");
        reportCommentEntity.setReason(ReportReason.MISINFORMATION);
        reportCommentEntity.setReportedBy(user);
        reportCommentEntity.setComment(comment);
        reportCommentEntity.setCreatedAt(LocalDateTime.now());

        return reportCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser("john@gmail.com");
        topic = createTopic(user);
        tags = createTags();
        comment = createComment(user, topic);
        reportComment = createReportComment(user, comment);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @Test
    public void ReportCommentService_CreateReportComment_ThrowBadRequestException() {
        CreateReportCommentRequest request = new CreateReportCommentRequest();
        request.setDetails("details");
        request.setReason(ReportReason.MISINFORMATION);
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());

        when(reportCommentRepository.findReportCommentByCommentIdAndUserId(comment.getId(), user.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() ->{
             reportCommentService.createReportComment(request);
        }).isInstanceOf(BadRequestException.class).hasMessage("You have already reported this comment");
    }


}



