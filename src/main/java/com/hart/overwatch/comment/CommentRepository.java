package com.hart.overwatch.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.comment.dto.CommentDto;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT NEW com.hart.overwatch.comment.dto.CommentDto(
             c.id AS id, c.content AS content, u.id AS userId,
             c.createdAt AS createdAt, p.avatarUrl AS avatarUrl,
             u.fullName AS fullName
            ) FROM Comment c
            INNER JOIN c.topic t
            INNER JOIN c.user u
            INNER JOIN c.user.profile p
            """)
    Page<CommentDto> getCommentsByTopicId(@Param("topicId") Long topicId,
            @Param("pageable") Pageable pageable);
}
