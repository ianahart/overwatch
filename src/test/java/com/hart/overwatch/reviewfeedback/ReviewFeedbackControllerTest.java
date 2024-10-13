package com.hart.overwatch.reviewfeedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.repository.dto.RepositoryContentsDto;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.dto.RepositoryReviewDto;
import com.hart.overwatch.repository.request.CreateRepositoryFileRequest;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryCommentRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewRequest;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;
import com.hart.overwatch.reviewfeedback.request.CreateReviewFeedbackRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.hamcrest.CoreMatchers;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = ReviewFeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewFeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewFeedbackService reviewFeedbackService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User owner;

    private User reviewer;

    private Repository repository;

    private ReviewFeedback reviewFeedback;

    private User createOwner() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setAvatarUrl("http://avatar.url/1");

        owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        owner.setId(1L);

        return owner;
    }

    private User createReviewer() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setId(2L);
        profile.setAvatarUrl("http://avatar.url/2");

        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        reviewer.setId(2L);

        return reviewer;
    }

    private Repository createRepository(User owner, User reviewer) {
        Repository repository = new Repository();
        LocalDateTime dateTime = LocalDateTime.now();

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);
        repository.setReviewStartTime(dateTime);
        repository.setReviewEndTime(dateTime);

        repository.setId(1L);

        return repository;
    }

    private ReviewFeedback createReviewFeedback(User owner, User reviewer, Repository repository) {
        ReviewFeedback reviewFeedback = new ReviewFeedback();
        reviewFeedback.setClarity(1);
        reviewFeedback.setThoroughness(1);
        reviewFeedback.setResponseTime(1);
        reviewFeedback.setHelpfulness(1);
        reviewFeedback.setOwner(owner);
        reviewFeedback.setReviewer(reviewer);
        reviewFeedback.setRepository(repository);

        reviewFeedback.setId(1L);

        return reviewFeedback;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewFeedback = createReviewFeedback(owner, reviewer, repository);
    }

    @Test
    public void ReviewFeedbackController_CreateReviewFeedback_ReturnCreateReviewFeedbackResponse()
            throws Exception {
        CreateReviewFeedbackRequest request = new CreateReviewFeedbackRequest();
        request.setOwnerId(owner.getId());
        request.setReviewerId(reviewer.getId());
        request.setRepositoryId(repository.getId());
        request.setClarity(reviewFeedback.getClarity());
        request.setHelpfulness(reviewFeedback.getHelpfulness());
        request.setResponseTime(reviewFeedback.getResponseTime());
        request.setThoroughness(reviewFeedback.getThoroughness());

        doNothing().when(reviewFeedbackService).createReviewFeedback(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/review-feedbacks").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void ReviewFeedbackController_GetSingleReviewFeedback_ReturnGetSingleReviewFeedbackResponse()
            throws Exception {
        Long ownerId = owner.getId();
        Long reviewerId = reviewer.getId();
        Long repositoryId = repository.getId();
        ReviewFeedbackDto reviewFeedbackDto = new ReviewFeedbackDto();

        reviewFeedbackDto.setId(reviewFeedback.getId());
        reviewFeedbackDto.setOwnerId(ownerId);
        reviewFeedbackDto.setReviewerId(reviewerId);
        reviewFeedbackDto.setRepositoryId(repositoryId);
        reviewFeedbackDto.setClarity(1);
        reviewFeedbackDto.setResponseTime(1);
        reviewFeedbackDto.setHelpfulness(1);
        reviewFeedbackDto.setThoroughness(1);
        reviewFeedbackDto.setCreatedAt(LocalDateTime.now());
        when(reviewFeedbackService.getSingleReviewFeedback(ownerId, reviewerId, repositoryId))
                .thenReturn(reviewFeedbackDto);

        ResultActions response = mockMvc.perform(get("/api/v1/review-feedbacks/single")
                .param("ownerId", "1").param("repositoryId", "1").param("reviewerId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }


}


