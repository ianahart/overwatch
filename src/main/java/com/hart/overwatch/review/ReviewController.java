package com.hart.overwatch.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.review.request.CreateReviewRequest;
import com.hart.overwatch.review.request.UpdateReviewRequest;
import com.hart.overwatch.review.response.CreateReviewResponse;
import com.hart.overwatch.review.response.DeleteReviewResponse;
import com.hart.overwatch.review.response.GetReviewResponse;
import com.hart.overwatch.review.response.GetReviewsResponse;
import com.hart.overwatch.review.response.UpdateReviewResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping(path = "")
    public ResponseEntity<GetReviewsResponse> getReviews(@RequestParam("userId") Long userId,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetReviewsResponse("success",
                this.reviewService.getReviews(userId, page, pageSize, direction)));
    }

    @PostMapping(path = "")
    ResponseEntity<CreateReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        this.reviewService.createReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateReviewResponse("success"));
    }

    @GetMapping(path = "/{reviewId}")
    ResponseEntity<GetReviewResponse> getReview(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetReviewResponse("success", this.reviewService.getReview(reviewId)));
    }

    @PatchMapping(path = "/{reviewId}")
    ResponseEntity<UpdateReviewResponse> updateReview(@PathVariable("reviewId") Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        this.reviewService.updateReview(reviewId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateReviewResponse("success"));
    }

    @DeleteMapping(path = "/{reviewId}")
    ResponseEntity<DeleteReviewResponse> deleteReview(@PathVariable("reviewId") Long reviewId) {
        this.reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteReviewResponse("success"));
    }
}
