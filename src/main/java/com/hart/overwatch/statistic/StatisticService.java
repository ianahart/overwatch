package com.hart.overwatch.statistic;

import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.repository.RepositoryService;
import com.hart.overwatch.repository.dto.CompletedRepositoryReviewDto;
import com.hart.overwatch.reviewfeedback.ReviewFeedbackService;
import com.hart.overwatch.statistic.dto.CompletedReviewStatDto;
import com.hart.overwatch.statistic.dto.OverallStatDto;
import com.hart.overwatch.statistic.dto.ReviewTypeStatDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.user.UserService;

@Service
public class StatisticService {

    private final ReviewFeedbackService reviewFeedbackService;

    private final RepositoryService repositoryService;

    private final UserService userService;

    @Autowired
    public StatisticService(ReviewFeedbackService reviewFeedbackService,
            RepositoryService repositoryService, UserService userService) {
        this.reviewFeedbackService = reviewFeedbackService;
        this.repositoryService = repositoryService;
        this.userService = userService;
    }


    private List<ReviewTypeStatDto> getReviewTypesCompleted(Long reviewerId) {
        List<CompletedRepositoryReviewDto> reviews =
                repositoryService.getCompletedReviews(reviewerId);
        return reviews.stream()
                .collect(Collectors.groupingBy(CompletedRepositoryReviewDto::getReviewType))
                .entrySet().stream()
                .<ReviewTypeStatDto>map(
                        entry -> new ReviewTypeStatDto(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
    }

    private List<CompletedReviewStatDto> getReviewsCompleted(Long reviewerId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                .atTime(LocalTime.MAX);

        List<CompletedRepositoryReviewDto> reviews =
                repositoryService.getCompletedReviews(reviewerId);

        return reviews.stream()
                .filter(review -> review.getReviewEndTime().isAfter(startOfMonth.minusSeconds(1))
                        && review.getReviewEndTime().isBefore(endOfMonth.plusSeconds(1)))
                .collect(Collectors.groupingBy(review -> review.getReviewEndTime().getDayOfMonth()))
                .entrySet().stream()
                .map(entry -> new CompletedReviewStatDto(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());

    }

    private List<Map<String, Object>> getAverageReviewTimes(Long reviewerId) {
        List<CompletedRepositoryReviewDto> reviews =
                repositoryService.getCompletedReviews(reviewerId);
        Map<YearMonth, List<CompletedRepositoryReviewDto>> reviewsByMonth =
                reviews.stream().collect(
                        Collectors.groupingBy(review -> YearMonth.from(review.getReviewEndTime())));
        Map<YearMonth, Double> averageReviewTimeByMonth = reviewsByMonth.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToLong(review -> Duration.between(review.getReviewStartTime(),
                                        review.getReviewEndTime()).toMinutes())
                                .average().orElse(0)));

        return averageReviewTimeByMonth.entrySet().stream().map(entry -> {
            Map<String, Object> data = new HashMap<>();
            data.put("month", entry.getKey().toString());
            data.put("avgReviewTime", entry.getValue());
            return data;
        }).collect(Collectors.toList());
    }

    public OverallStatDto getStatistics(Long reviewerId) {
        if (reviewerId == null) {
            throw new BadRequestException("Missing parameter: reviewerId");
        }
        List<CompletedReviewStatDto> reviewsCompleted = getReviewsCompleted(reviewerId);
        List<ReviewTypeStatDto> reviewTypesCompleted = getReviewTypesCompleted(reviewerId);
        List<Map<String, Object>> avgReviewTimes = getAverageReviewTimes(reviewerId);
        return new OverallStatDto(reviewsCompleted, reviewTypesCompleted, avgReviewTimes);
    }
}
