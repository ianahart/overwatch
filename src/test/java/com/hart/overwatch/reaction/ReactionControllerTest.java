package com.hart.overwatch.reaction;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.doNothing;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reaction.request.CreateReactionRequest;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = ReactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReactionService reactionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void ReactionController_CreateReaction_ReturnCreateReactionResponse() throws Exception {
        CreateReactionRequest request = new CreateReactionRequest();
        Comment newComment = new Comment();
        newComment.setId(2L);
        Long commentId = newComment.getId();
        request.setUserId(user.getId());
        request.setEmoji("😢");

        doNothing().when(reactionService).createReaction(request, commentId);

        ResultActions response =
                mockMvc.perform(post(String.format("/api/v1/comments/%d/reactions", commentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void ReactionController_GetReaction_ReturnGetReactionResponse() throws Exception{
          when(reactionService.getReaction(comment.getId(), user.getId())).thenReturn("😢");
           ResultActions response = mockMvc.perform(get(String.format("/api/v1/comments/%d/reactions", comment.getId())).param("userId", "1"));

           response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void ReactionController_DeleteReaction_ReturnDeleteReactionResponse() throws Exception {
        Long commentId = comment.getId();
        Long userId = user.getId();
        doNothing().when(reactionService).deleteReaction(commentId, userId);

        ResultActions response = mockMvc.perform(
                delete(String.format("/api/v1/comments/%d/reactions/%d", commentId, userId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

}

