package com.hart.overwatch.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {


    Reaction findByCommentIdAndUserId(Long commentId, Long userId);

    @Query(value = """
            SELECT EXISTS( SELECT 1 FROM Reaction r
                 INNER JOIN r.user u
                INNER JOIN r.comment c
                WHERE u.id = :userId
                AND c.id = :commentId
                )
            """)
    Boolean existsByUserIdAndCommentId(@Param("userId") Long userId,
            @Param("commentId") Long commentId);
}
