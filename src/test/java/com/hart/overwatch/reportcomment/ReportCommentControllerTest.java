package com.hart.overwatch.reportcomment;


import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.hamcrest.CoreMatchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = ReportCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReportCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportCommentService reportCommentService;

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

    private ReportComment reportComment;

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

    private ReportComment createReportComment(User user, Comment comment) {
        ReportComment reportCommentEntity = new ReportComment();
        reportCommentEntity.setId(1L);
        reportCommentEntity.setReason(ReportReason.MISINFORMATION);
        reportCommentEntity.setDetails("details");
        reportCommentEntity.setComment(comment);
        reportCommentEntity.setReportedBy(user);
        return reportCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        reportComment = createReportComment(user, comment);

    }

    @Test
    public void ReportCommentControllerTest_CreateReportComment_ReturnCreateReportCommentResponse()
            throws Exception {
        CreateReportCommentRequest request = new CreateReportCommentRequest();
        Comment newComment = new Comment();
        newComment.setId(2L);
        newComment.setContent("some content");

        request.setUserId(user.getId());
        request.setCommentId(newComment.getId());
        request.setDetails("details");
        request.setReason(ReportReason.MISINFORMATION);

        doNothing().when(reportCommentService).createReportComment(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/report-comments").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

}


