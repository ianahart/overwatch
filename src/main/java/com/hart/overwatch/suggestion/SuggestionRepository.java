package com.hart.overwatch.suggestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.suggestion.dto.SuggestionDto;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {


    @Query(value = """
            SELECT new com.hart.overwatch.suggestion.dto.SuggestionDto(
            s.id AS id, s.createdAt AS createdAt, s.title AS title,
            s.description AS description, s.contact AS contact,
            s.fileUrl AS fileUrl, s.feedbackType AS feedbackType,
            s.priorityLevel AS priorityLevel, s.feedbackStatus AS feedbackStatus,
            u.fullName AS fullName, p.avatarUrl AS avatarUrl
            ) FROM Suggestion s
            INNER JOIN s.user u
            INNER JOIN s.user.profile p
            WHERE :feedbackStatus IS NULL OR s.feedbackStatus = :feedbackStatus
            """)
    Page<SuggestionDto> getAllSuggestions(@Param("pageable") Pageable pageable,
            @Param("feedbackStatus") FeedbackStatus feedbackStatus);
}
