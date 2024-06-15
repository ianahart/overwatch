package com.hart.overwatch.review;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.review.request.CreateReviewRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }


    private Review getReviewById(Long reviewId) {
        try {
            return this.reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException(
                    String.format("A review with the id %d was not found", reviewId)));

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Float getAvgReviewRating(Long reviewerId) {
        try {
            return this.reviewRepository.getAvgRatingByReviewerId(reviewerId);

        } catch (DataAccessException ex) {

            throw new BadRequestException("Could not get avg review rating");
        }
    }

    public Long getTotalCountOfReviews(Long reviewerId) {
        try {
            return this.reviewRepository.getTotalReviewsCount(reviewerId);

        } catch (DataAccessException ex) {

            return 0L;
        }
    }

    public void createReview(CreateReviewRequest request) {
        try {
            Boolean authorHasAlreadyReviewed =
                    this.reviewRepository.getReviewByReviewerIdAndAuthorId(request.getReviewerId(),
                            request.getAuthorId());

            if (authorHasAlreadyReviewed) {
                throw new BadRequestException("You have already reviewed this reviewer");
            }

            User author = this.userService.getUserById(request.getAuthorId());
            User reviewer = this.userService.getUserById(request.getReviewerId());

            Review review = new Review(author, reviewer, false, request.getRating(),
                    Jsoup.clean(request.getReview(), Safelist.none()));


            this.reviewRepository.save(review);

        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Cannot insert duplicate review");
        }
    }
}
