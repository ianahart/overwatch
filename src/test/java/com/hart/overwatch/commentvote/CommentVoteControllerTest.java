package com.hart.overwatch.commentvote;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.commentvote.request.CreateCommentVoteRequest;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.dto.TagDto;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topicmanagement.TopicManagementService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = CommentVoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentVoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentVoteRepository commentVoteRepository;

    @MockBean
    private CommentVoteService commentVoteService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void CommentVoteController_CreateCommentVote_ReturnCreateCommentVoteResponse()
            throws Exception {
        CreateCommentVoteRequest request = new CreateCommentVoteRequest();
        Comment commentWithoutVote = new Comment();
        commentWithoutVote.setId(2L);
        request.setCommentId(commentWithoutVote.getId());
        request.setUserId(user.getId());
        request.setVoteType("UPVOTE");

        doNothing().when(commentVoteService).createCommentVote(request);

        ResultActions response = mockMvc.perform(
                post(String.format("/api/v1/comments/%d/votes", commentWithoutVote.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }
}


