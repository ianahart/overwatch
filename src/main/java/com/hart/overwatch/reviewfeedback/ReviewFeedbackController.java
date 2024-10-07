package com.hart.overwatch.reviewfeedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.reviewfeedback.request.CreateReviewFeedbackRequest;
import com.hart.overwatch.reviewfeedback.response.CreateReviewFeedbackResponse;
import com.hart.overwatch.reviewfeedback.response.GetSingleReviewFeedbackResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/review-feedbacks")
public class ReviewFeedbackController {

    private final ReviewFeedbackService reviewFeedbackService;

    @Autowired
    public ReviewFeedbackController(ReviewFeedbackService reviewFeedbackService) {
        this.reviewFeedbackService = reviewFeedbackService;
    }

    @PostMapping("")
    ResponseEntity<CreateReviewFeedbackResponse> createReviewFeedback(
            @Valid @RequestBody CreateReviewFeedbackRequest request) {
        reviewFeedbackService.createReviewFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateReviewFeedbackResponse("success"));
    }

    @GetMapping("/single")
    ResponseEntity<GetSingleReviewFeedbackResponse> getSingleReviewFeedback(
            @RequestParam("ownerId") Long ownerId, @RequestParam("reviewerId") Long reviewerId,
            @RequestParam("repositoryId") Long repositoryId) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetSingleReviewFeedbackResponse(
                "success",
                reviewFeedbackService.getSingleReviewFeedback(ownerId, reviewerId, repositoryId)));
    }

}
