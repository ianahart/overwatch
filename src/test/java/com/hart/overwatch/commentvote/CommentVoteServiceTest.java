package com.hart.overwatch.commentvote;

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
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.topic.Topic;
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
    private CommentService commentService;

    @Mock
    private CommentVoteRepository commentVoteRepository;

    @Mock
    private UserService userService;

    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private CommentVote commentVote;

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
        commentEntity.setTopic(topic);
        commentEntity.setUser(user);
        commentEntity.setContent(String.format("content"));
        commentEntity.setIsEdited(false);
        commentEntity.setId(1L);

        return commentEntity;
    }

    private CommentVote createCommentVote(User user, Comment comment) {
        CommentVote commentVoteEntity = new CommentVote();
        commentVoteEntity.setId(1L);
        commentVoteEntity.setVoteType("UPVOTE");
        commentVoteEntity.setCreatedAt(LocalDateTime.now());
        commentVoteEntity.setUser(user);
        commentVoteEntity.setComment(comment);

        return commentVoteEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser("john@gmail.com");
        topic = createTopic(user);
        tags = createTags();
        comment = createComment(user, topic);
        commentVote = createCommentVote(user, comment);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @Test
    public void CommentVoteService_CreateCommentVote_ThrowBadRequestException() {
        CreateCommentVoteRequest request = new CreateCommentVoteRequest();
        request.setCommentId(comment.getId());
        request.setUserId(user.getId());
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
        Comment commentWithoutVote = new Comment();
        commentWithoutVote.setId(2L);
        request.setCommentId(commentWithoutVote.getId());
        request.setUserId(user.getId());
        request.setVoteType("UPVOTE");

        when(commentVoteRepository.findByCommentIdAndUserId(commentWithoutVote.getId(),
                user.getId())).thenReturn(false);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(commentService.getCommentById(commentWithoutVote.getId()))
                .thenReturn(commentWithoutVote);
        CommentVote newCommentVote = new CommentVote();
        newCommentVote.setId(2L);
        newCommentVote.setUser(user);
        newCommentVote.setComment(commentWithoutVote);
        when(commentVoteRepository.save(any(CommentVote.class))).thenReturn(new CommentVote());

        commentVoteService.createCommentVote(request);

        verify(commentVoteRepository, times(1)).save(any(CommentVote.class));

        Assertions.assertThatNoException();
    }

}


