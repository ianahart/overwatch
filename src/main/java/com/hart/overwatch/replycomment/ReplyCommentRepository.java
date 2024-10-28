package com.hart.overwatch.replycomment;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {


    @Query(value = """
            SELECT COUNT(rc) FROM ReplyComment rc
            INNER JOIN rc.user u
            WHERE u.id = :userId
            AND rc.createdAt > :timeLimit
            """)
    int countByUserIdAndCreatedAtAfter(@Param("userId") Long userId,
            @Param("timeLimit") LocalDateTime timeLimit);

}
