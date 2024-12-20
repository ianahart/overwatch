package com.hart.overwatch.reviewerbadge;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.badge.Badge;
import com.hart.overwatch.badge.BadgeService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class ReviewerBadgeService {

    private final ReviewerBadgeRepository reviewerBadgeRepository;

    private final UserService userService;

    private final BadgeService badgeService;

    private final PaginationService paginationService;

    @Autowired
    public ReviewerBadgeService(ReviewerBadgeRepository reviewerBadgeRepository,
            UserService userService, BadgeService badgeService,
            PaginationService paginationService) {
        this.reviewerBadgeRepository = reviewerBadgeRepository;
        this.userService = userService;
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
}
