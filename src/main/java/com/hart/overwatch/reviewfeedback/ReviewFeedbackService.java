package com.hart.overwatch.reviewfeedback;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.repository.Repository;
import com.hart.overwatch.repository.RepositoryService;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackRatingsDto;
import com.hart.overwatch.reviewfeedback.request.CreateReviewFeedbackRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class ReviewFeedbackService {

    private final ReviewFeedbackRepository reviewFeedbackRepository;

    private final UserService userService;

    private final RepositoryService repositoryService;


    @Autowired
    public ReviewFeedbackService(ReviewFeedbackRepository reviewFeedbackRepository,
            UserService userService, RepositoryService repositoryService) {
        this.reviewFeedbackRepository = reviewFeedbackRepository;
        this.userService = userService;
        this.repositoryService = repositoryService;
    }


    private boolean alreadyGivenFeedback(Long ownerId, Long reviewerId, long repositoryId) {
        return reviewFeedbackRepository.findByOwnerIdAndReviewerIdAndRepositoryId(ownerId,
                reviewerId, repositoryId);
    }

    public ReviewFeedbackDto getSingleReviewFeedback(Long ownerId, Long reviewerId,
            Long repositoryId) {
        if (ownerId == null || reviewerId == null || repositoryId == null) {
            throw new BadRequestException(
                    "Missing parameters: ownerId, reviewerId, or repositoryId");
        }
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (!ownerId.equals(currentUser.getId())) {
            throw new ForbiddenException("You are unauthorized to see feedback that is not yours");
        }

        return reviewFeedbackRepository.getSingleReviewFeedback(ownerId, reviewerId, repositoryId);
    }

    public void createReviewFeedback(CreateReviewFeedbackRequest request) {
        Long ownerId = request.getOwnerId();
        Long reviewerId = request.getReviewerId();
        Long repositoryId = request.getRepositoryId();

        if (ownerId == null || reviewerId == null || repositoryId == null) {
            throw new BadRequestException(
                    "Missing parameters: ownerId, reviewerId, or repositoryId");
        }

        if (alreadyGivenFeedback(ownerId, reviewerId, repositoryId)) {
            throw new BadRequestException("You have already given feedback for this review");
        }

        User owner = userService.getUserById(ownerId);
        User reviewer = userService.getUserById(reviewerId);
        Repository repository = repositoryService.getRepositoryById(repositoryId);

        if (!repository.getOwner().getId().equals(owner.getId())) {
            throw new ForbiddenException(
                    "You are not authorized to submit feedback forthis repository.");
        }

        ReviewFeedback reviewFeedback = new ReviewFeedback();
        reviewFeedback.setClarity(request.getClarity());
        reviewFeedback.setHelpfulness(request.getHelpfulness());
        reviewFeedback.setThoroughness(request.getThoroughness());
        reviewFeedback.setResponseTime(request.getResponseTime());
        reviewFeedback.setOwner(owner);
        reviewFeedback.setReviewer(reviewer);
        reviewFeedback.setRepository(repository);

        reviewFeedbackRepository.save(reviewFeedback);
    }

    public List<ReviewFeedbackRatingsDto> getReviewFeedbackRatings(Long reviewerId) {
        return reviewFeedbackRepository.getReviewFeedbackRatings(reviewerId);
    }

}
