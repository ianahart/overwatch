package com.hart.overwatch.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

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
