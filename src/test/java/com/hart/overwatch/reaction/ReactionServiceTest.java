package com.hart.overwatch.reaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reaction.request.CreateReactionRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReactionServiceTest {

    @InjectMocks
    private ReactionService reactionService;

    @Mock
    ReactionRepository reactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;


    private User user;

    private Comment comment;

    private Reaction reaction;



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


    private Reaction createReaction(User user, Comment comment) {
        LocalDateTime createdAt = LocalDateTime.now();
        Reaction reactionEntity = new Reaction();
        reactionEntity.setId(1L);
        reactionEntity.setEmoji("👍");
        reactionEntity.setUser(user);
        reactionEntity.setComment(comment);
        reactionEntity.setCreatedAt(createdAt);

        return reactionEntity;

    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        reaction = createReaction(user, comment);
    }

    @Test
    public void ReactionService_CreateReaction_ThrowBadRequestException() {
        CreateReactionRequest request = new CreateReactionRequest();
        Long commentId = comment.getId();
        request.setUserId(user.getId());
        request.setEmoji("👍");

        when(reactionRepository.existsByUserIdAndCommentId(request.getUserId(), commentId))
                .thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            reactionService.createReaction(request, commentId);
        }).isInstanceOf(BadRequestException.class).hasMessage("You already reacted with %s",
                request.getEmoji());
    }

    @Test
    public void ReactionService_CreateReaction_ReturnNothing() {
        CreateReactionRequest request = new CreateReactionRequest();
        Comment newComment = new Comment();
        newComment.setId(2L);
        Long commentId = newComment.getId();
        request.setUserId(user.getId());
        request.setEmoji("😢");

        when(reactionRepository.existsByUserIdAndCommentId(request.getUserId(), commentId))
                .thenReturn(false);
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(commentService.getCommentById(commentId)).thenReturn(newComment);

        reactionService.createReaction(request, commentId);

        ArgumentCaptor<Reaction> reactionCaptor = ArgumentCaptor.forClass(Reaction.class);
        verify(reactionRepository, times(1)).save(reactionCaptor.capture());

        Reaction capturedReaction = reactionCaptor.getValue();
        Assertions.assertThat(capturedReaction.getUser()).isEqualTo(user);
        Assertions.assertThat(capturedReaction.getComment()).isEqualTo(newComment);
        Assertions.assertThat(capturedReaction.getEmoji()).isEqualTo("😢");

        Assertions.assertThatNoException();
    }

    @Test
    public void ReactionService_GetReaction_ThrowBadRequestException() {
        Long userId = user.getId();
        Long commenId = null;

        Assertions.assertThatThrownBy(() -> {
            reactionService.getReaction(commenId, userId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing either comment id or user id");
    }

    @Test
    public void ReationService_GetReaction_ReturnStringEmoji() {
        Long commentId = comment.getId();
        Long userId = user.getId();

        when(reactionRepository.findByCommentIdAndUserId(commentId, userId)).thenReturn(reaction);

        String result = reactionService.getReaction(commentId, userId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(reaction.getEmoji());
    }

    @Test
    public void ReationService_GetReaction_ReturnNull() {
        Comment newComment = new Comment();
        newComment.setId(2L);
        Long userId = user.getId();

        when(reactionRepository.findByCommentIdAndUserId(newComment.getId(), userId))
                .thenReturn(null);

        String result = reactionService.getReaction(newComment.getId(), userId);
        Assertions.assertThat(result).isNull();;
    }

    @Test
    public void ReactionService_DeleteReaction_ThrowBadRequestException() {
        Long commentId = null;
        Long userId = user.getId();

        Assertions.assertThatThrownBy(() -> {
            reactionService.deleteReaction(commentId, userId);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing either comment id or user id");
    }

    @Test
    public void ReactionService_DeleteReaction_ReturnNothing() {
        Long commentId = comment.getId();
        Long userId = user.getId();

        when(reactionRepository.findByCommentIdAndUserId(commentId, userId)).thenReturn(reaction);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        doNothing().when(reactionRepository).delete(reaction);
        reactionService.deleteReaction(commentId, userId);

        Assertions.assertThatNoException();
        verify(reactionRepository, times(1)).delete(reaction);
    }

}


