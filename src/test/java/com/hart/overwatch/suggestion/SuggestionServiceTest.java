package com.hart.overwatch.suggestion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
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
}


