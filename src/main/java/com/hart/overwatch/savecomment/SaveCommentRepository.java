package com.hart.overwatch.savecomment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveCommentRepository extends JpaRepository<SaveComment, Long> {

    @Query(value = """
            SELECT EXISTS (SELECT 1 FROM SaveComment sc
             INNER JOIN sc.user u
             INNER JOIN sc.comment c
             WHERE u.id = :userId
             AND c.id = :commentId
            )
            """)
    Boolean findSaveCommentByUserIdAndCommentId(@Param("userId") Long userId,
            @Param("commentId") Long commentId);
}
