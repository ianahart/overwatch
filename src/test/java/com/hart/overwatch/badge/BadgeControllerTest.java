package com.hart.overwatch.badge;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.badge.dto.BadgeDto;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = BadgeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BadgeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BadgeService badgeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Badge badge;


    private Badge createBadge() {
        Badge badgeEntity = new Badge();
        badgeEntity.setId(1L);
        badgeEntity.setTitle("First Reviewer Badge");
        badgeEntity.setImageUrl("https://www.imgur.com/photo-1");
        badgeEntity.setDescription("description");
        badgeEntity.setImageFileName("photo-1");
        badgeEntity.setReviewerBadges(List.of());

        return badgeEntity;
    }

    private BadgeDto convertToDto(Badge badge) {
        BadgeDto badgeDto = new BadgeDto();
        badgeDto.setId(badge.getId());
        badgeDto.setTitle(badge.getTitle());
        badgeDto.setImageUrl(badge.getImageUrl());
        badgeDto.setDescription(badge.getDescription());

        return badgeDto;
    }

    @BeforeEach
    public void setUp() {
        badge = createBadge();
    }


    @Test
    public void BadgeController_CreateBadge_ReturnCreateBadgeResponse() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("attachment", "image.jpg",
                MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        ResultActions response = mockMvc.perform(multipart("/api/v1/admin/badges").file(mockFile)
                .param("title", "title").param("description", "description")
                .contentType(MediaType.MULTIPART_FORM_DATA));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }

    @Test
    public void BadgeController_GetBadges_ReturnGetAllBadgesResponse() throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = PageRequest.of(page, pageSize, Sort.unsorted());
        BadgeDto badgeDto = convertToDto(badge);
        Page<BadgeDto> pageResult = new PageImpl<>(List.of(badgeDto), pageable, 1);
        PaginationDto<BadgeDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(badgeService.getBadges(page, pageSize, direction)).thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/admin/badges").param("page", "0")
                .param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));


    }
}


