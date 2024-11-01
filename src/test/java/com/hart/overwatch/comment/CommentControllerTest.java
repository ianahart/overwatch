package com.hart.overwatch.comment;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.comment.dto.MinCommentDto;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.comment.request.UpdateCommentRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reaction.dto.ReactionDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Test
    public void CommentController_GetAllCommentResponse_ReturnGetAllCommentResponse()
            throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String sort = "vote";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<CommentDto> commentDtos =
                comments.stream().map(this::convertToDto).collect(Collectors.toList());
        Page<CommentDto> pageResult = new PageImpl<>(commentDtos, pageable, commentDtos.size());

        PaginationDto<CommentDto> expectedPaginationDto =
                new PaginationDto<>(commentDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(commentService.getComments(topic.getId(), page, pageSize, direction, sort))
                .thenReturn(expectedPaginationDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/topics/%d/comments", topic.getId()))
                        .param("page", "0").param("pageSize", "3").param("direction", direction)
                        .param("sort", "vote"));


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(commentDtos.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(commentDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) expectedPaginationDto.getTotalElements())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(expectedPaginationDto.getDirection())));
    }

    @Test
    public void CommentController_CreateComment_ReturnCreateCommentResponse() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setUserId(user.getId());
        request.setTopicId(topic.getId());
        request.setContent("new content");

        doNothing().when(commentService).createComment(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/comments").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CommentController_UpdateComment_ReturnUpdateCommentResponse() throws Exception {
        Comment comment = comments.get(0);
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUserId(user.getId());
        request.setContent("updated content");

        doNothing().when(commentService).updateComment(request, comment.getId());

        ResultActions response =
                mockMvc.perform(patch(String.format("/api/v1/comments/%d", comment.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CommentController_DeleteComment_ReturnDeleteCommentResponse() throws Exception {
        Long commentId = comments.get(0).getId();

        doNothing().when(commentService).deleteComment(commentId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/comments/%d", commentId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CommentController_GetComment_ReturnGetCommentResponse() throws Exception {
        Comment comment = comments.get(0);

        MinCommentDto minCommentDto = new MinCommentDto();
        minCommentDto.setId(comment.getId());
        minCommentDto.setUserId(comment.getUser().getId());
        minCommentDto.setContent(comment.getContent());
        minCommentDto.setCreatedAt(LocalDateTime.now());
        minCommentDto.setAvatarUrl(comment.getUser().getProfile().getAvatarUrl());
        minCommentDto.setFullName(comment.getUser().getFullName());

        when(commentService.getComment(comment.getId())).thenReturn(minCommentDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/comments/%d", comment.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(comment.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId",
                        CoreMatchers.is(comment.getUser().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content",
                        CoreMatchers.is(comment.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.avatarUrl",
                        CoreMatchers.is(comment.getUser().getProfile().getAvatarUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fullName",
                        CoreMatchers.is(comment.getUser().getFullName())));
    }

}


