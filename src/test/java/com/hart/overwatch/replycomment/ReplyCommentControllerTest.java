package com.hart.overwatch.replycomment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.replycomment.dto.ReplyCommentDto;
import com.hart.overwatch.replycomment.request.CreateReplyCommentRequest;
import com.hart.overwatch.replycomment.request.UpdateReplyCommentRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = ReplyCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReplyCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReplyCommentService replyCommentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    private ReplyCommentDto convertToDto(ReplyComment replyComment) {
        ReplyCommentDto replyCommentDto = new ReplyCommentDto();
        replyCommentDto.setId(replyComment.getId());
        replyCommentDto.setUserId(replyComment.getUser().getId());
        replyCommentDto.setContent(replyComment.getContent());
        replyCommentDto.setFullName(replyComment.getUser().getFullName());
        replyCommentDto.setAvatarUrl(replyComment.getUser().getProfile().getAvatarUrl());
        replyCommentDto.setCreatedAt(LocalDateTime.now());

        return replyCommentDto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        replyComment = createReplyComment(user, comment);

    }

    @Test
    public void ReplyCommentController_GetReplyComments_ReturnGetReplyCommentsResponse()
            throws Exception {
        Long commentId = comment.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<ReplyCommentDto> replyCommentDtos =
                List.of(replyComment).stream().map(this::convertToDto).collect(Collectors.toList());
        Page<ReplyCommentDto> pageResult =
                new PageImpl<>(replyCommentDtos, pageable, replyCommentDtos.size());
        PaginationDto<ReplyCommentDto> expectedPaginationDto =
                new PaginationDto<>(replyCommentDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(replyCommentService.getReplyComments(commentId, page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/comments/%d/reply", commentId))
                        .param("page", "0").param("pageSize", "3").param("direction", "next"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(replyCommentDtos.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(replyCommentDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) expectedPaginationDto.getTotalElements())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(expectedPaginationDto.getDirection())));

    }


    @Test
    public void ReplyCommentController_GetReplyCommentsByUserAndComment_ReturnGetReplyCommentsByUserAndCommentResponse()
            throws Exception {
        Long userId = user.getId();
        Long commentId = comment.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<ReplyCommentDto> replyCommentDtos =
                List.of(replyComment).stream().map(this::convertToDto).collect(Collectors.toList());
        Page<ReplyCommentDto> pageResult =
                new PageImpl<>(replyCommentDtos, pageable, replyCommentDtos.size());
        PaginationDto<ReplyCommentDto> expectedPaginationDto =
                new PaginationDto<>(replyCommentDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(replyCommentService.getReplyCommentsByUserAndComment(userId, commentId, page, pageSize,
                direction)).thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/comments/%d/reply/user/%d", commentId, userId))
                        .param("page", "0").param("pageSize", "3").param("direction", "next"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(replyCommentDtos.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(replyCommentDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) expectedPaginationDto.getTotalElements())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(expectedPaginationDto.getDirection())));

    }

    @Test
    public void ReplyCommentController_CreateRepyComment_ReturnCreateReplyCommentResponse()
            throws Exception {
        CreateReplyCommentRequest request = new CreateReplyCommentRequest();
        request.setUserId(user.getId());
        request.setContent("new content");
        Long commentId = comment.getId();

        doNothing().when(replyCommentService).createReplyComment(request, commentId);

        ResultActions response =
                mockMvc.perform(post(String.format("/api/v1/comments/%d/reply", commentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void ReplyCommentController_UpdateReplyComment_ReturnUpdateReplyCommentResponse()
            throws Exception {
        UpdateReplyCommentRequest request = new UpdateReplyCommentRequest("updated content");
        when(replyCommentService.updateReplyComment(anyLong(),
                any(UpdateReplyCommentRequest.class))).thenReturn("updated content");

        ResultActions response = mockMvc.perform(patch(String.format("/api/v1/comments/%d/reply/%d",
                comment.getId(), replyComment.getId())).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                        CoreMatchers.is(request.getContent())));
    }

    @Test
    public void ReplyCommentController_DeleteReplyComment_ReturnDeleteReplyCommentResponse()
            throws Exception {

        doNothing().when(replyCommentService).deleteReplyComment(replyComment.getId());

        ResultActions response = mockMvc.perform(delete(String
                .format("/api/v1/comments/%d/reply/%d", comment.getId(), replyComment.getId())));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

}


