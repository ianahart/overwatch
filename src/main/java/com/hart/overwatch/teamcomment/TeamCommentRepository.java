package com.hart.overwatch.teamcomment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teamcomment.dto.TeamCommentDto;

@Repository
public interface TeamCommentRepository extends JpaRepository<TeamComment, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.teamcomment.dto.TeamCommentDto(
             tc.id AS id, u.id AS userId, tc.content AS content, tc.createdAt,
             u.fullName AS fullName, p.avatarUrl AS avatarUrl, tp.id AS teamPostId,
             tc.isEdited AS isEdited, tc.tag AS tag
            ) FROM TeamComment tc
            INNER JOIN tc.user u
            INNER JOIN tc.user.profile p
            INNER JOIN tc.teamPost tp
            WHERE tp.id = :teamPostId
            """)
    Page<TeamCommentDto> getTeamCommentsByTeamPostId(@Param("pageable") Pageable pageable,
            @Param("teamPostId") Long teamPostId);
}
