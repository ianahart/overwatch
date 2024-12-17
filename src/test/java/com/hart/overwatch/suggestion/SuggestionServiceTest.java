package com.hart.overwatch.suggestion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.suggestion.dto.SuggestionDto;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
import com.hart.overwatch.suggestion.request.UpdateSuggestionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SuggestionServiceTest {

    @InjectMocks
    private SuggestionService suggestionService;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private UserService useService;

    @Mock
    private AmazonService amazonService;

    @Mock
    private PaginationService paginationService;


    private User user;

    private List<Suggestion> suggestions = new ArrayList<>();

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setId(1L);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
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
    public void SuggestionService_CreateSuggestion_ReturnNothing() {
        CreateSuggestionRequest request = new CreateSuggestionRequest();
        request.setTitle("title");
        request.setUserId(request.getUserId());
        request.setContact("contact");
        request.setDescription("description");
        request.setFeedbackType(FeedbackType.FEATURE_REQUEST);
        request.setPriorityLevel(PriorityLevel.LOW);
        request.setAttachment(new MockMultipartFile("file", "test-image.jpeg", "image/jpeg",
                new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A,
                        0x46, 0x49, 0x46, 0x00, 0x01}));

        HashMap<String, String> result = new HashMap<>();
        result.put("filename", "filename");
        result.put("objectUrl", "https://www.s3.com/photo-1.jpeg");

        when(amazonService.putS3Object("arrow-date", request.getAttachment().getOriginalFilename(),
                request.getAttachment())).thenReturn(result);

        Suggestion newSuggestion = new Suggestion();
        newSuggestion.setTitle(request.getTitle());
        newSuggestion.setDescription(request.getDescription());
        newSuggestion.setContact(request.getContact());
        newSuggestion.setFileName(result.get("filename"));
        newSuggestion.setFileUrl(result.get("objectUrl"));
        newSuggestion.setFeedbackType(request.getFeedbackType());
        newSuggestion.setPriorityLevel(request.getPriorityLevel());
        newSuggestion.setFeedbackStatus(FeedbackStatus.PENDING);
        newSuggestion.setUser(user);

        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(newSuggestion);

        suggestionService.createSuggestion(request);

        verify(amazonService, times(1)).putS3Object("arrow-date",
                request.getAttachment().getOriginalFilename(), request.getAttachment());
        verify(suggestionRepository, times(1)).save(any(Suggestion.class));

        Assertions.assertThatNoException();
    }

    @Test
    public void SuggestionService_GetAllSuggestions_ReturnPaginationDtoOfSuggestionDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        List<SuggestionDto> suggestionDtos = convertToDto(suggestions);
        Page<SuggestionDto> pageResult = new PageImpl<>(suggestionDtos, pageable, 1);
        PaginationDto<SuggestionDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(suggestionRepository.getAllSuggestions(pageable, FeedbackStatus.PENDING))
                .thenReturn(pageResult);

        PaginationDto<SuggestionDto> actualPaginationDto = suggestionService
                .getAllSuggestions(FeedbackStatus.PENDING, page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto).isNotNull();
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(suggestions.size());
        List<SuggestionDto> actualSuggestionDtos = actualPaginationDto.getItems();
        List<SuggestionDto> expectedSuggestionDtos = expectedPaginationDto.getItems();

        for (int i = 0; i < actualSuggestionDtos.size(); i++) {
            Assertions.assertThat(actualSuggestionDtos.get(i).getId())
                    .isEqualTo(expectedSuggestionDtos.get(i).getId());
            Assertions.assertThat(actualSuggestionDtos.get(i).getTitle())
                    .isEqualTo(expectedSuggestionDtos.get(i).getTitle());
            Assertions.assertThat(actualSuggestionDtos.get(i).getContact())
                    .isEqualTo(expectedSuggestionDtos.get(i).getContact());
            Assertions.assertThat(actualSuggestionDtos.get(i).getFileUrl())
                    .isEqualTo(expectedSuggestionDtos.get(i).getFileUrl());
            Assertions.assertThat(actualSuggestionDtos.get(i).getFullName())
                    .isEqualTo(expectedSuggestionDtos.get(i).getFullName());
            Assertions.assertThat(actualSuggestionDtos.get(i).getAvatarUrl())
                    .isEqualTo(expectedSuggestionDtos.get(i).getAvatarUrl());
            Assertions.assertThat(actualSuggestionDtos.get(i).getDescription())
                    .isEqualTo(expectedSuggestionDtos.get(i).getDescription());
            Assertions.assertThat(actualSuggestionDtos.get(i).getFeedbackType())
                    .isEqualTo(expectedSuggestionDtos.get(i).getFeedbackType());
            Assertions.assertThat(actualSuggestionDtos.get(i).getPriorityLevel())
                    .isEqualTo(expectedSuggestionDtos.get(i).getPriorityLevel());
            Assertions.assertThat(actualSuggestionDtos.get(i).getFeedbackStatus())
                    .isEqualTo(expectedSuggestionDtos.get(i).getFeedbackStatus());
        }
    }

    @Test
    public void SuggestionService_UpdateSuggestion_ReturnFeedbackStatus() {
        UpdateSuggestionRequest request = new UpdateSuggestionRequest();
        request.setFeedbackStatus(FeedbackStatus.IMPLEMENTED);
        Suggestion suggestionToUpdate = suggestions.get(0);

        when(suggestionRepository.findById(suggestionToUpdate.getId()))
                .thenReturn(Optional.of(suggestionToUpdate));

        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestionToUpdate);

        FeedbackStatus result = suggestionService.updateSuggestion(request, suggestionToUpdate.getId());

        verify(suggestionRepository, times(1)).save(any(Suggestion.class));
        Assertions.assertThat(result).isEqualTo(request.getFeedbackStatus());
    }
}


