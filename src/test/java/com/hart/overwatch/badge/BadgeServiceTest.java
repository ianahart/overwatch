package com.hart.overwatch.badge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockMultipartFile;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BadgeServiceTest {

    private final String BUCKET_NAME = "arrow-date";

    @InjectMocks
    private BadgeService badgeService;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    @Mock
    private AmazonService amazonService;

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

    @BeforeEach
    public void setUp() {
        badge = createBadge();
    }

    @Test
    public void BadgeService_CreateBadge_ThrowBadRequestException() {
        CreateBadgeRequest request = new CreateBadgeRequest();
        request.setTitle("First Reviewer Badge");
        request.setDescription("description");
        request.setImage(new MockMultipartFile("file", "test-image.jpeg", "image/jpeg",
                new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A,
                        0x46, 0x49, 0x46, 0x00, 0x01}));

        when(badgeRepository.existsByTitle(request.getTitle().toLowerCase())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> {
            badgeService.createBadge(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(String
                .format("You have already created a badge with the title %s", request.getTitle()));
    }

}


