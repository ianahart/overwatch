package com.hart.overwatch.comment;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.dto.CommentDto;
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
@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

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



}


