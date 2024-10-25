package com.hart.overwatch.commentvote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    @Query(value = """
            SELECT EXISTS( SELECT 1 FROM CommentVote cv
                INNER JOIN cv.comment c
                INNER JOIN cv.user u
                WHERE c.id = :commentId
                AND u.id = :userId
                )
            """)
    boolean findByCommentIdAndUserId(@Param("commentId") Long commentId,
            @Param("userId") Long userId);
}
