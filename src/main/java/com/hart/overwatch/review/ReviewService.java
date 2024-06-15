package com.hart.overwatch.review;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.review.dto.MinReviewDto;
import com.hart.overwatch.review.dto.ReviewDto;
import com.hart.overwatch.review.request.CreateReviewRequest;
import com.hart.overwatch.review.request.UpdateReviewRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService,
            PaginationService paginationService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.paginationService = paginationService;
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


    public PaginationDto<ReviewDto> getReviews(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<ReviewDto> result = this.reviewRepository.getAllReviewsByReviewerId(pageable, userId);


        return new PaginationDto<ReviewDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }


    public MinReviewDto getReview(Long reviewId) {
        Review review = getReviewById(reviewId);

        return new MinReviewDto(review.getId(), review.getRating(), review.getReview());
    }


    public void updateReview(Long reviewId, UpdateReviewRequest request) {
        try {

            User user = this.userService.getCurrentlyLoggedInUser();

            if (user.getId() != request.getAuthorId()) {
                throw new ForbiddenException("You cannot edit another person's review");
            }
            Review review = getReviewById(reviewId);

            review.setRating(request.getRating());
            review.setReview(Jsoup.clean(request.getReview(), Safelist.none()));

            this.reviewRepository.save(review);


        } catch  (DataIntegrityViolationException ex) {
            throw new BadRequestException("You have already reviewed this reviewer");
        }
    }
}
