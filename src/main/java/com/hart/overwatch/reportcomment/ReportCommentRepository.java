package com.hart.overwatch.reportcomment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

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
