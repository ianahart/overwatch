package com.hart.overwatch.suggestion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.suggestion.dto.SuggestionDto;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
@WebMvcTest(controllers = SuggestionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuggestionService suggestionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private List<Suggestion> suggestions = new ArrayList<>();

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(1L);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.ADMIN, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }


    private List<SuggestionDto> convertToDto(List<Suggestion> suggestions) {
        List<SuggestionDto> suggestionDtos = new ArrayList<>();
        for (var suggestion : suggestions) {
            SuggestionDto suggestionDto = new SuggestionDto();
            suggestionDto.setId(suggestion.getId());
            suggestionDto.setTitle(suggestion.getTitle());
            suggestionDto.setContact(suggestion.getContact());
            suggestionDto.setFileUrl(suggestion.getFileUrl());
            suggestionDto.setFullName(suggestion.getUser().getFullName());
            suggestionDto.setAvatarUrl(suggestion.getUser().getProfile().getAvatarUrl());
            suggestionDto.setDescription(suggestion.getDescription());
            suggestionDto.setFeedbackType(suggestion.getFeedbackType());
            suggestionDto.setPriorityLevel(suggestion.getPriorityLevel());
            suggestionDto.setFeedbackStatus(suggestion.getFeedbackStatus());
            suggestionDtos.add(suggestionDto);
        }
        return suggestionDtos;
    }

    private List<Suggestion> createSuggestions(User user, int numOfSuggestions) {
        List<Suggestion> suggestionEntities = new ArrayList<>();
        for (int i = 0; i < numOfSuggestions; i++) {
            Suggestion suggestionEntity = new Suggestion();
            suggestionEntity.setId(Long.valueOf(i + 1));
            suggestionEntity.setUser(user);
            suggestionEntity.setTitle("title");
            suggestionEntity.setContact("contact");
            suggestionEntity.setFileUrl("https://www.s3.com");
            suggestionEntity.setFileName("filename");
            suggestionEntity.setDescription("description");
            suggestionEntity.setFeedbackType(FeedbackType.FEATURE_REQUEST);
            suggestionEntity.setPriorityLevel(PriorityLevel.LOW);
            suggestionEntity.setFeedbackStatus(FeedbackStatus.PENDING);
            suggestionEntities.add(suggestionEntity);
        }
        return suggestionEntities;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        int numOfSuggestions = 3;
        suggestions = createSuggestions(user, numOfSuggestions);
    }

@Test
public void SuggestionController_CreateSuggestion_ReturnCreateSuggestionResponse() throws Exception {
    // Create the mock multipart file (only for file uploads, if any)
    MockMultipartFile mockFile = new MockMultipartFile(
            "attachment", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

    // Perform the request
    ResultActions response = mockMvc.perform(
            multipart("/api/v1/suggestions")
                    .file(mockFile) // Add the file, if applicable
                    .param("title", "title") // Add the form data fields
                    .param("userId", String.valueOf(user.getId()))
                    .param("contact", "contact")
                    .param("description", "description")
                    .param("feedbackType", "GENERAL_FEEDBACK")
                    .param("priorityLevel", "LOW")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
    );

    // Verify the response
    response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
}

}


