package com.hart.overwatch.reviewfeedback;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackRatingsDto;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
    @Query(value = """
            SELECT new com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackRatingsDto(
              rfb.thoroughness AS thoroughness, rfb.helpfulness AS helpfulness,
              rfb.responseTime AS responseTime, rfb.clarity AS clarity
            ) FROM ReviewFeedback rfb
            INNER JOIN rfb.reviewer r
            WHERE r.id = :reviewerId
            """)
    List<ReviewFeedbackRatingsDto> getReviewFeedbackRatings(@Param("reviewerId") Long reviewerId);


    @Query(value = """
             SELECT new com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto(
              rf.id AS id, r.id AS repositoryId, o.id AS ownerId, re.id AS reviewerId,
              rf.clarity AS clarity, rf.responseTime as responseTime, rf.helpfulness AS helpfulness,
              rf.thoroughness AS thoroughness, rf.createdAt AS createdAt
             ) FROM ReviewFeedback rf
             INNER JOIN rf.owner o
             INNER JOIN rf.reviewer re
             INNER JOIN rf.repository r
             WHERE o.id = :ownerId
             AND re.id = :reviewerId
            AND r.id = :repositoryId
             """)
    ReviewFeedbackDto getSingleReviewFeedback(@Param("ownerId") Long ownerId,
            @Param("reviewerId") Long reviewerId, @Param("repositoryId") Long repositoryId);

    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM ReviewFeedback rf
                INNER JOIN rf.owner o
                INNER JOIN rf.reviewer re
                INNER JOIN rf.repository r
                WHERE o.id = :ownerId
                AND re.id = :reviewerId
                AND r.id = :repositoryId
                )
            """)
    boolean findByOwnerIdAndReviewerIdAndRepositoryId(@Param("ownerId") Long ownerId,
            @Param("reviewerId") Long reviewerId, @Param("repositoryId") Long repositoryId);


    @Query(value = """
                SELECT
                    rf.reviewer_id,
                    r.owner_id,
                    COUNT(r.id) AS repositories_reviewed,
                    AVG(rf.helpfulness) AS avg_helpfulness,
                    AVG(rf.clarity) AS avg_clarity,
                    AVG(rf.thoroughness) AS avg_thoroughness
                FROM
                    review_feedback rf
                JOIN
                    repository r ON rf.repository_id = r.id
                WHERE
                    r.status = 'COMPLETED'
                    AND rf.helpfulness >= 4
                    AND rf.clarity >= 4
                    AND rf.thoroughness >= 4
                GROUP BY
                    rf.reviewer_id, r.owner_id
                HAVING
                    COUNT(r.id) >= 2
            """, nativeQuery = true)
    List<Object[]> findPotentialMentors();
}
