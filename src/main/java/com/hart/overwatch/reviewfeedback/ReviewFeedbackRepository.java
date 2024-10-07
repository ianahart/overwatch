package com.hart.overwatch.reviewfeedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
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
}
