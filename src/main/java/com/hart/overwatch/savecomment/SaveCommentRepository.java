package com.hart.overwatch.savecomment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;

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


    @Query(value = """
                SELECT NEW com.hart.overwatch.savecomment.dto.SaveCommentDto(
                  sc.id AS id, c.content AS content, c.createdAt AS createdAt,
                  u.fullName AS fullName, p.avatarUrl AS avatarUrl, u.id AS userId
                ) FROM SaveComment sc
                INNER JOIN sc.user u
                INNER JOIN sc.user.profile p
                INNER JOIN sc.comment c
                WHERE u.id = :userId
            """)
    Page<SaveCommentDto> findSaveCommentsByUserId(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);


}
