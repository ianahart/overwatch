package com.hart.overwatch.savecomment;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = SaveCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SaveCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SaveCommentService saveCommentService;

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

    private SaveComment saveComment;

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


    private SaveComment createSaveComment(User user, Comment comment) {
        SaveComment saveCommentEntity = new SaveComment();
        saveCommentEntity.setId(1L);
        saveCommentEntity.setComment(comment);
        saveCommentEntity.setUser(user);
        return saveCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        saveComment = createSaveComment(user, comment);
    }

    @Test
    public void SaveCommentController_CreateSaveComment_ReturnCreateSaveCommentResponse()
            throws Exception {
        CreateSaveCommentRequest request = new CreateSaveCommentRequest();
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());

        doNothing().when(saveCommentService).createSaveComment(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/save-comments").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void SaveCommentController_GetSaveComments_ReturnGetSaveCommentsResponse()
            throws Exception {
        Long userId = user.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String sort = "vote";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        List<SaveCommentDto> saveCommentDtos = Collections.singletonList(new SaveCommentDto());
        Page<SaveCommentDto> pageResult =
                new PageImpl<>(saveCommentDtos, pageable, saveCommentDtos.size());

        PaginationDto<SaveCommentDto> expectedPaginationDto =
                new PaginationDto<>(saveCommentDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(saveCommentService.getSaveComments(userId, page, pageSize, direction))
                .thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/save-comments").param("userId", "1")
                .param("page", "0").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(saveCommentDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) expectedPaginationDto.getTotalElements())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(expectedPaginationDto.getDirection())));
    }

    @Test
    public void SaveCommentController_DeleteSaveComment_ReturnDeleteSaveCommentResponse()
            throws Exception {
        Long saveCommentId = saveComment.getId();
        doNothing().when(saveCommentService).deleteSaveComment(saveCommentId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/save-comments/%d", saveCommentId)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }
}


