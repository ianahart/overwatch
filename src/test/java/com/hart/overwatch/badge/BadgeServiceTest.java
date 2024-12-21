package com.hart.overwatch.badge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.badge.dto.BadgeDto;
import com.hart.overwatch.badge.dto.MinBadgeDto;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.badge.request.UpdateBadgeRequest;
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

    @Test
    public void BadgeService_CreateBadge_ReturnNothing() {
        CreateBadgeRequest request = new CreateBadgeRequest();
        request.setTitle("Second Reviewer Badge");
        request.setDescription("description");
        request.setImage(new MockMultipartFile("file", "test-image.jpeg", "image/jpeg",
                new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A,
                        0x46, 0x49, 0x46, 0x00, 0x01}));

        when(badgeRepository.existsByTitle(request.getTitle().toLowerCase())).thenReturn(false);
        HashMap<String, String> result = new HashMap<>();
        result.put("filename", "filename");
        result.put("objectUrl", "https://www.s3.com/photo-1.jpeg");
        when(amazonService.putS3Object(BUCKET_NAME, request.getImage().getOriginalFilename(),
                request.getImage())).thenReturn(result);

        Badge badge = new Badge();
        badge.setTitle(request.getTitle());
        badge.setDescription(request.getDescription());
        badge.setImageFileName(result.get("filename"));
        badge.setImageUrl(result.get("objectUrl"));

        when(badgeRepository.save(any(Badge.class))).thenReturn(badge);

        badgeService.createBadge(request);

        verify(amazonService, times(1)).putS3Object(BUCKET_NAME,

                request.getImage().getOriginalFilename(), request.getImage());
        verify(badgeRepository, times(1)).save(any(Badge.class));
    }

    @Test
    public void BadgeService_GetBadges_ReturnPaginationDtoOfBadges() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = PageRequest.of(page, pageSize, Sort.unsorted());
        BadgeDto badgeDto = convertToDto(badge);
        Page<BadgeDto> pageResult = new PageImpl<>(List.of(badgeDto), pageable, 1);
        PaginationDto<BadgeDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(badgeRepository.getBadges(pageable)).thenReturn(pageResult);

        PaginationDto<BadgeDto> actualPaginationDto =
                badgeService.getBadges(page, pageSize, direction);

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
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        BadgeDto actualBadgeDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualBadgeDto.getId()).isEqualTo(badgeDto.getId());
        Assertions.assertThat(actualBadgeDto.getTitle()).isEqualTo(badgeDto.getTitle());
        Assertions.assertThat(actualBadgeDto.getDescription()).isEqualTo(badgeDto.getDescription());
        Assertions.assertThat(actualBadgeDto.getImageUrl()).isEqualTo(badgeDto.getImageUrl());
    }

    @Test
    public void BadgeService_GetBadge_ThrowNotFoundException() {
        Long badgeId = null;

        Assertions.assertThatThrownBy(() -> {
            badgeService.getBadge(badgeId);
        }).isInstanceOf(NotFoundException.class).hasMessage("Could not find data for badge");
    }

    @Test
    public void BadgeService_GetBadge_ReturnMinBadgeDto() {
        Long badgeId = badge.getId();

        when(badgeRepository.findById(badgeId)).thenReturn(Optional.of(badge));

        MinBadgeDto minBadgeDto = badgeService.getBadge(badgeId);

        Assertions.assertThat(minBadgeDto).isNotNull();
        Assertions.assertThat(minBadgeDto.getImage()).isEqualTo(badge.getImageUrl());
        Assertions.assertThat(minBadgeDto.getTitle()).isEqualTo(badge.getTitle());
        Assertions.assertThat(minBadgeDto.getDescription()).isEqualTo(badge.getDescription());
    }

    @Test
    public void BadgeService_UpdateBadge_ThrowBadRequestException() {
        UpdateBadgeRequest request = new UpdateBadgeRequest();
        request.setTitle("Second Reviewer Badge");
        request.setDescription("description");
        request.setImage(new MockMultipartFile("file", "test-image.jpeg", "image/jpeg",
                new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A,
                        0x46, 0x49, 0x46, 0x00, 0x01}));

        when(badgeRepository.existsByTitle(request.getTitle().toLowerCase())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            badgeService.updateBadge(request, badge.getId());
        }).isInstanceOf(BadRequestException.class).hasMessage(String
                .format("You have already created a badge with the title %s", request.getTitle()));
    }

    @Test
    public void BadgeService_UpdateBadge_WithImage_ReturnNothing() {
        UpdateBadgeRequest request = new UpdateBadgeRequest();
        request.setTitle("Second Reviewer Badge");
        request.setDescription("description");
        request.setImage(new MockMultipartFile("file", "test-image.jpeg", "image/jpeg",
                new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A,
                        0x46, 0x49, 0x46, 0x00, 0x01}));

        when(badgeRepository.existsByTitle(request.getTitle().toLowerCase())).thenReturn(false);
        when(badgeRepository.findById(badge.getId())).thenReturn(Optional.of(badge));

        doNothing().when(amazonService).deleteBucketObject(BUCKET_NAME, "photo-1");
        HashMap<String, String> result = new HashMap<>();
        result.put("filename", "filename");
        result.put("objectUrl", "https://www.s3.com/photo-1.jpeg");
        when(amazonService.putS3Object(BUCKET_NAME, request.getImage().getOriginalFilename(),
                request.getImage())).thenReturn(result);

        when(badgeRepository.save(any(Badge.class))).thenReturn(badge);

        badgeService.updateBadge(request, badge.getId());

        verify(amazonService, times(1)).deleteBucketObject(BUCKET_NAME, "photo-1");
        verify(amazonService, times(1)).putS3Object(BUCKET_NAME,
                request.getImage().getOriginalFilename(), request.getImage());
        verify(badgeRepository, times(1)).save(any(Badge.class));
    }


    @Test
    public void BadgeService_UpdateBadge_WithNoImage_ReturnNothing() {
        UpdateBadgeRequest request = new UpdateBadgeRequest();
        request.setTitle("Second Reviewer Badge");
        request.setDescription("description");

        when(badgeRepository.existsByTitle(request.getTitle().toLowerCase())).thenReturn(false);
        when(badgeRepository.findById(badge.getId())).thenReturn(Optional.of(badge));


        when(badgeRepository.save(any(Badge.class))).thenReturn(badge);

        badgeService.updateBadge(request, badge.getId());
        verify(badgeRepository, times(1)).save(any(Badge.class));
    }

    @Test
    public void BadgeService_DeleteBadge_ReturnNothing() {
        when(badgeRepository.findById(badge.getId())).thenReturn(Optional.of(badge));
        doNothing().when(amazonService).deleteBucketObject(BUCKET_NAME, badge.getImageFileName());
        doNothing().when(badgeRepository).delete(badge);

        badgeService.deleteBadge(badge.getId());
        verify(amazonService, times(1)).deleteBucketObject(BUCKET_NAME, badge.getImageFileName());
        verify(badgeRepository, times(1)).delete(badge);
    }

    @Test
    public void BadgeService_FindBadgeByTitle_ReturnOptionalBadge() {
        String badgeTitle = badge.getTitle().toLowerCase();
        when(badgeRepository.findBadgeByTitle(badgeTitle)).thenReturn(Optional.of(badge));

        Optional<Badge> result = badgeService.findBadgeByTitle(badgeTitle);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getTitle()).isEqualTo(badge.getTitle());
    }
}


