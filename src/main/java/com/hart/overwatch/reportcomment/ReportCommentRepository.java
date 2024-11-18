package com.hart.overwatch.reportcomment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.reportcomment.dto.ReportCommentDto;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.reportcomment.dto.ReportCommentDto(
            rc.id AS id, rc.details AS details, rc.reason AS reason, rc.status AS status,
            rb.fullName AS reportedBy, rc.createdAt AS createdAt, c.content AS content,
            p.avatarUrl AS commentAvatarUrl, t.title AS topicTitle
            ) FROM ReportComment rc
            LEFT JOIN rc.comment c
            LEFT JOIN rc.comment.user u
            LEFT JOIN rc.comment.user.profile p
            LEFT JOIN rc.comment.topic t
            INNER JOIN rc.reportedBy rb
            """)
    Page<ReportCommentDto> getReportComments(@Param("pageable") Pageable pageable);

    @Query(value = """
            SELECT EXISTS( SELECT 1 FROM ReportComment rc
              INNER JOIN rc.comment c
              INNER JOIN rc.reportedBy u
              WHERE c.id = :commentId
              AND u.id = :userId
            )
            """)
    Boolean findReportCommentByCommentIdAndUserId(@Param("commentId") Long commentId,
            @Param("userId") Long userId);
}
