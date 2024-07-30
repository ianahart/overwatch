package com.hart.overwatch.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.favorite.request.ToggleFavoriteRequest;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.review.dto.MinReviewDto;
import com.hart.overwatch.review.dto.ReviewDto;
import com.hart.overwatch.review.request.CreateReviewRequest;
import com.hart.overwatch.review.request.UpdateReviewRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@ActiveProfiles("test")
@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User author;

    private User reviewer;

    private Review review;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        Boolean isEdited = true;
        Byte rating = 5;
        String reviewContent = "This is a review";
        author = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        review = new Review(author, reviewer, isEdited, rating,
                Jsoup.clean(reviewContent, Safelist.none()));

        author.setId(1L);
        reviewer.setId(2L);
        review.setId(1L);
    }

    @Test
    public void ReviewController_GetReview_ReturnReviewResponse() throws Exception {

        MinReviewDto minReviewDto =
                new MinReviewDto(review.getId(), review.getRating(), review.getReview());

        when(reviewService.getReview(review.getId())).thenReturn(minReviewDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/reviews/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        Matchers.equalToObject(minReviewDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.rating",
                        CoreMatchers.equalToObject(minReviewDto.getRating().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.review",
                        CoreMatchers.is(minReviewDto.getReview())));
    }

    @Test
    public void ReviewController_CreateReview_ReturnCreateReviewResponse() throws Exception {
        Byte rating = 5;
        String reviewContent = "Some content";
        CreateReviewRequest request = new CreateReviewRequest(1L, 2L, rating, reviewContent);

        doNothing().when(reviewService).createReview(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/reviews").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    void ReviewController_GetAllReviewsByReviewerId_ReturnReviewsResponse() throws Exception {
        Long reviewerId = reviewer.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        ReviewDto reviewDto = new ReviewDto();
        Page<ReviewDto> pageResult =
                new PageImpl<>(Collections.singletonList(reviewDto), pageable, 1);
        PaginationDto<ReviewDto> paginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(reviewService.getReviews(reviewerId, page, pageSize, direction))
                .thenReturn(paginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON).param("userId", String.valueOf(reviewerId))
                .param("page", String.valueOf(page)).param("pageSize", String.valueOf(pageSize))
                .param("direction", direction));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(reviewDto.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(pageResult.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(pageResult.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(direction)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) pageResult.getTotalElements())));
    }

    @Test
    public void ReviewController_UpdateReview_ReturnUpdateReviewResponse() throws Exception {
        Byte rating = 4;
        String reviewContent = "New content";
        UpdateReviewRequest request = new UpdateReviewRequest(1L, 2L, rating, reviewContent);

        doNothing().when(this.reviewService).updateReview(review.getId(), request);

        ResultActions response =
                mockMvc.perform(patch("/api/v1/reviews/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void ReviewController_DeleteReview_ReturnDeleteReviewResponse() throws Exception {

        doNothing().when(this.reviewService).deleteReview(review.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/reviews/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }


}


