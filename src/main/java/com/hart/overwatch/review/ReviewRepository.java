package com.hart.overwatch.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.review.dto.ReviewDto;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.review.dto.ReviewDto(
            r.id AS id, a.id AS authorId, p.avatarUrl AS avatarUrl,
            r.rating AS rating, r.review AS review, r.createdAt AS createdAt,
            r.isEdited AS isEdited, p.fullName AS name
            ) FROM Review r
            INNER JOIN r.author a
            INNER JOIN r.reviewer rr
            INNER JOIN r.author.profile p
            WHERE rr.id = :reviewerId
            """)
    Page<ReviewDto> getAllReviewsByReviewerId(@Param("pageable") Pageable pageable,
            @Param("reviewerId") Long reviewerId);

    @Query(value = """
             SELECT EXISTS(SELECT 1 FROM
                Review r
                INNER JOIN r.author a
                INNER JOIN r.reviewer rr
                WHERE a.id = :authorId
                AND rr.id = :reviewerId
                )
            """)
    Boolean getReviewByReviewerIdAndAuthorId(@Param("reviewerId") Long reviewerId,
            @Param("authorId") Long authorId);



    @Query(value = """
                SELECT AVG(r.rating) FROM Review r
                INNER JOIN r.reviewer rr
                WHERE rr.id = :reviewerId
            """)
    Float getAvgRatingByReviewerId(@Param("reviewerId") Long reviewerId);

    @Query(value = """
            SELECT COUNT(r.id) FROM Review r
            INNER JOIN r.reviewer rr
            WHERE rr.id = :reviewerId
            """)
    Long getTotalReviewsCount(@Param("reviewerId") Long reviewerId);
}
