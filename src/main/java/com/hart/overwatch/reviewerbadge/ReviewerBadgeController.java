package com.hart.overwatch.reviewerbadge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.reviewerbadge.response.GetAllReviewerBadgesResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class ReviewerBadgeController {


    private final ReviewerBadgeService reviewerBadgeService;

    @Autowired
    public ReviewerBadgeController(ReviewerBadgeService reviewerBadgeService) {
        this.reviewerBadgeService = reviewerBadgeService;
    }

    @GetMapping(path = "/reviewer-badges")
    public ResponseEntity<GetAllReviewerBadgesResponse> getBadges(
            @RequestParam("reviewerId") Long reviewerId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllReviewerBadgesResponse("success",
                reviewerBadgeService.getBadges(reviewerId, page, pageSize, direction)));
    }
}
