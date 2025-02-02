package com.hart.overwatch.testimonial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.testimonial.dto.TestimonialDto;
import com.hart.overwatch.testimonial.request.CreateTestimonialRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.hamcrest.CoreMatchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = TestimonialController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestimonialService testimonialService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private List<TestimonialDto> testimonialsDto = new ArrayList<>();

    private List<Testimonial> testimonials = new ArrayList<>();


    @BeforeEach
    public void init() {
        user = new User("bill@mail.com", "bill", "smith", "bill smith", Role.REVIEWER, true,
                new Profile(), "password123", new Setting());
        user.setId(1L);

        List<String> testimonialNames = Arrays.asList("john doe", "jane doe", "paul smith");
        List<String> testimonialTexts = Arrays.asList("test 1", "test 2", "test 3");


        IntStream.rangeClosed(0, 2).forEach(i -> {
            Testimonial testimonial =
                    new Testimonial(testimonialNames.get(i), testimonialTexts.get(i), false, user);
            testimonials.add(testimonial);
        });

        testimonialsDto =
                Arrays.asList(new TestimonialDto(1L, user.getId(), testimonials.get(0).getName(),
                        testimonials.get(0).getText(), testimonials.get(0).getCreatedAt()));


    }


    @Test
    public void TestimonialController_DeleteTestimonial_RetunOk() throws Exception {

        doNothing().when(testimonialService).deleteTestimonial(testimonials.get(0).getId());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/testimonials/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TestimonialController_CreateTestimonial_ReturnCreated() throws Exception {
        CreateTestimonialRequest request = new CreateTestimonialRequest(user.getId(),

                testimonials.get(0).getName(), testimonials.get(0).getText());


        ResultActions response =
                mockMvc.perform(post("/api/v1/testimonials").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }


    @Test
    public void TestimonialController_GetTestimonials_ReturnGetTestimonialsResponse()
            throws Exception {

        Long userId = user.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        TestimonialDto testimonialDto = new TestimonialDto();
        testimonialDto.setId(1L);
        Page<TestimonialDto> pageResult =
                new PageImpl<>(Collections.singletonList(testimonialDto), pageable, 1);
        PaginationDto<TestimonialDto> paginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(testimonialService.getTestimonials(userId, page, pageSize, direction))
                .thenReturn(paginationDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/testimonials").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(userId)).param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)).param("direction", direction));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.equalToObject(testimonialDto.getId().intValue())))
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
    public void TestimonialController_GetLatestTestimonial_ReturnGetLatestTestionalsResponse() throws Exception {

        when(testimonialService.getLatestTestimonials(user.getId())).thenReturn(testimonialsDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/testimonials/latest").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success"))).andExpect(MockMvcResultMatchers
            .jsonPath("$.data[0].id", CoreMatchers.equalToObject(testimonialsDto.get(0).getId().intValue())));


    }

}


