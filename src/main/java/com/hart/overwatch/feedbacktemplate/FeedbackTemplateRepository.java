package com.hart.overwatch.feedbacktemplate;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto;
import com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto;

public interface FeedbackTemplateRepository extends JpaRepository<FeedbackTemplate, Long> {

    @Query(value = """
            SELECT COUNT(ft.id) FROM FeedbackTemplate ft
            INNER JOIN ft.user u
            WHERE u.id = :userId
            """)
    int countFeedbackTemplatesByUserId(@Param("userId") Long userId);

    @Query(value = """
                SELECT new com.hart.overwatch.feedbacktemplate.dto.MinFeedbackTemplateDto(
                ft.id AS id, u.id AS userId
                ) FROM FeedbackTemplate ft
                INNER JOIN ft.user u
                WHERE u.id = :userId
            """)
    List<MinFeedbackTemplateDto> getFeedbackTemplates(@Param("userId") Long userId);

    @Query(value = """
                SELECT new com.hart.overwatch.feedbacktemplate.dto.FeedbackTemplateDto(
                ft.id AS id, u.id AS userId, ft.feedback AS feedback) FROM FeedbackTemplate ft
                INNER JOIN ft.user u
                WHERE ft.id = :feedbackTemplateId
            """)
    FeedbackTemplateDto getFeedbackTemplate(@Param("feedbackTemplateId") Long feedbackTemplateId);
}
