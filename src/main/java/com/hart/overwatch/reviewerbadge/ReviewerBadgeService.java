package com.hart.overwatch.reviewerbadge;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.badge.Badge;
import com.hart.overwatch.badge.BadgeService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.reviewerbadge.dto.ReviewerBadgeDto;
import com.hart.overwatch.user.User;

@Service
public class ReviewerBadgeService {

    private final ReviewerBadgeRepository reviewerBadgeRepository;

    private final BadgeService badgeService;

    private final PaginationService paginationService;

    @Autowired
    public ReviewerBadgeService(ReviewerBadgeRepository reviewerBadgeRepository,
            BadgeService badgeService, PaginationService paginationService) {
        this.reviewerBadgeRepository = reviewerBadgeRepository;
        this.badgeService = badgeService;
        this.paginationService = paginationService;
    }


    public boolean hasBadge(Long reviewerId, String badgeTitle) {
        return reviewerBadgeRepository.existsByReviewerIdAndBadgeTitle(reviewerId, badgeTitle);
    }

    public void createBadge(User reviewer, String badgeTitle) {
        Optional<Badge> badge = badgeService.findBadgeByTitle(badgeTitle);

        if (badge.isPresent()) {
            ReviewerBadge reviewerBadge = new ReviewerBadge(reviewer, badge.get());
            reviewerBadgeRepository.save(reviewerBadge);
        }
    }

    public PaginationDto<ReviewerBadgeDto> getBadges(Long reviewerId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<ReviewerBadgeDto> result =
                this.reviewerBadgeRepository.getReviewerBadgesByReviewerId(pageable, reviewerId);
        return new PaginationDto<ReviewerBadgeDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

}
