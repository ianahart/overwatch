package com.hart.overwatch.badge;

import java.util.HashMap;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.badge.dto.BadgeDto;
import com.hart.overwatch.badge.dto.MinBadgeDto;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.badge.request.UpdateBadgeRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class BadgeService {

    private final String BUCKET_NAME = "arrow-date";

    private final AmazonService amazonService;

    private final BadgeRepository badgeRepository;

    private final PaginationService paginationService;

    @Autowired
    public BadgeService(AmazonService amazonService, BadgeRepository badgeRepository,
            PaginationService paginationService) {
        this.amazonService = amazonService;
        this.badgeRepository = badgeRepository;
        this.paginationService = paginationService;
    }

    private Badge getBadgeById(Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find badge with id %d", badgeId)));
    }


    private boolean badgeAlreadyExists(String title) {
        return badgeRepository.existsByTitle(title.toLowerCase());
    }

    public void createBadge(CreateBadgeRequest request) {
        String title = Jsoup.clean(request.getTitle(), Safelist.none());
        String description = Jsoup.clean(request.getDescription(), Safelist.none());
        MultipartFile image = request.getImage();

        if (badgeAlreadyExists(title)) {
            throw new BadRequestException(
                    String.format("You have already created a badge with the title %s", title));
        }

        HashMap<String, String> result =
                amazonService.putS3Object(BUCKET_NAME, image.getOriginalFilename(), image);
        String imageFileName = result.get("filename");
        String imageUrl = result.get("objectUrl");

        Badge badge = new Badge(imageFileName, imageUrl, title, description);

        badgeRepository.save(badge);
    }

    public PaginationDto<BadgeDto> getBadges(int page, int pageSize, String direction) {
        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<BadgeDto> result = this.badgeRepository.getBadges(pageable);

        return new PaginationDto<BadgeDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

    private MinBadgeDto convertToDto(Badge badge) {
        MinBadgeDto dto = new MinBadgeDto();
        dto.setTitle(badge.getTitle());
        dto.setDescription(badge.getDescription());
        dto.setImage(badge.getImageUrl());

        return dto;
    }

    public MinBadgeDto getBadge(Long badgeId) {
        if (badgeId == null) {
            throw new NotFoundException("Could not find data for badge");
        }

        Badge badge = getBadgeById(badgeId);

        return convertToDto(badge);
    }

    public void updateBadge(UpdateBadgeRequest request, Long badgeId) {
        String title = Jsoup.clean(request.getTitle(), Safelist.none());
        String description = Jsoup.clean(request.getDescription(), Safelist.none());
        MultipartFile image = request.getImage();

        if (badgeAlreadyExists(title)) {
            throw new BadRequestException(
                    String.format("You have already created a badge with the title %s", title));
        }

        Badge badge = getBadgeById(badgeId);
        if (image != null) {
            amazonService.deleteBucketObject(BUCKET_NAME, badge.getImageFileName());

            HashMap<String, String> result =
                    amazonService.putS3Object(BUCKET_NAME, image.getOriginalFilename(), image);
            String imageFileName = result.get("filename");
            String imageUrl = result.get("objectUrl");

            badge.setImageFileName(imageFileName);
            badge.setImageUrl(imageUrl);

        }
        badge.setTitle(title);
        badge.setDescription(description);

        badgeRepository.save(badge);
    }

    public void deleteBadge(Long badgeId) {
        Badge badge = getBadgeById(badgeId);

        amazonService.deleteBucketObject(BUCKET_NAME, badge.getImageFileName());

        badgeRepository.delete(badge);
    }

    public Optional<Badge> findBadgeByTitle(String badgeTitle) {
        return badgeRepository.findBadgeByTitle(badgeTitle.toLowerCase());
    }

}
