package com.hart.overwatch.commentvote;


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
import com.hart.overwatch.commentvote.request.CreateCommentVoteRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentVoteServiceTest {

    @InjectMocks
    private CommentVoteService commentVoteService;

    @Mock
    CommentVoteRepository commentVoteRepository;

    @Mock
    TopicService topicService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    private User user;

    private Comment comment;

    private CommentVote commentVote;

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


    private CommentVote createCommentVote(User user, Comment comment) {
        CommentVote commentVoteEntity = new CommentVote(comment, user, "UPVOTE");
        commentVoteEntity.setId(1L);
        return commentVoteEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        commentVote = createCommentVote(user, comment);
    }

    @Test
    public void CommentVoteService_CreateCommentVote_ThrowBadRequestException() {
        CreateCommentVoteRequest request = new CreateCommentVoteRequest();
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());
        request.setVoteType("UPVOTE");

        when(commentVoteRepository.findByCommentIdAndUserId(comment.getId(), user.getId()))
                .thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            commentVoteService.createCommentVote(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already voted on this comment");
    }

    @Test
    public void CommentVoteService_CreateCommentVote_ReturnNothing() {
        CreateCommentVoteRequest request = new CreateCommentVoteRequest();
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());
        request.setVoteType("UPVOTE");

        when(commentVoteRepository.findByCommentIdAndUserId(comment.getId(), user.getId()))
                .thenReturn(false);

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(commentService.getCommentById(comment.getId())).thenReturn(comment);

        CommentVote newCommentVote = new CommentVote(comment, user, request.getVoteType());

        when(commentVoteRepository.save(any(CommentVote.class))).thenReturn(newCommentVote);

        commentVoteService.createCommentVote(request);

        Assertions.assertThatNoException();
        verify(commentVoteRepository, times(1)).save(any(CommentVote.class));
    }


}


