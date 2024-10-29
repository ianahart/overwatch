package com.hart.overwatch.replycomment;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.replycomment.dto.ReplyCommentDto;

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

    @Query(value = """
                SELECT NEW com.hart.overwatch.replycomment.dto.ReplyCommentDto(
                  rc.id AS id, rc.content AS content, rc.createdAt AS createdAt,
                  u.fullName AS fullName, p.avatarUrl AS avatarUrl, u.id AS userId
                ) FROM ReplyComment rc
                INNER JOIN rc.user u
                INNER JOIN rc.user.profile p
                INNER JOIN rc.comment c
                WHERE u.id = :userId
                AND c.id = :commentId
            """)
    Page<ReplyCommentDto> findReplyCommentsByUserIdAndCommentId(
            @Param("pageable") Pageable pageable, @Param("userId") Long userId,
            @Param("commentId") Long commentId);
}
