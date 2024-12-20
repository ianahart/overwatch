package com.hart.overwatch.reviewerbadge;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.reviewerbadge.dto.ReviewerBadgeDto;

@Repository
public interface ReviewerBadgeRepository extends JpaRepository<ReviewerBadge, Long> {

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM ReviewerBadge rb
             INNER JOIN rb.reviewer r
             INNER JOIN rb.badge b
             WHERE r.id = :reviewerId
             AND b.title = :badgeTitle
            )
                """)
    boolean existsByReviewerIdAndBadgeTitle(@Param("reviewerId") Long reviewerId,
            @Param("badgeTitle") String badgeTitle);

    @Query(value = """
                SELECT NEW com.hart.overwatch.reviewerbadge.dto.ReviewerBadgeDto(
                  rb.id AS id, r.id AS reviewerId, b.id AS badgeId, rb.createdAt AS createdAt,
                  b.title AS title, b.description AS description, b.imageUrl AS imageUrl
                ) FROM ReviewerBadge rb
                INNER JOIN rb.reviewer r
                INNER JOIN rb.badge b
                WHERE r.id = :reviewerId
            """)
    Page<ReviewerBadgeDto> getReviewerBadgesByReviewerId(@Param("pageable") Pageable pageable,
            @Param("reviewerId") Long reviewerId);
}
