package com.hart.overwatch.teampost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teampost.dto.TeamPostDto;

@Repository
public interface TeamPostRepository extends JpaRepository<TeamPost, Long> {


    @Query(value = """
            SELECT new com.hart.overwatch.teampost.dto.TeamPostDto(
             tp.id AS id, t.id AS teamId, tp.createdAt AS createdAt,
             u.id AS userId, tp.code AS code, tp.isEdited AS isEdited,
             u.fullName AS fullName, p.avatarUrl AS avatarUrl, tp.language AS language
            ) FROM TeamPost tp
            INNER JOIN tp.team t
            INNER JOIN tp.user u
            INNER JOIN tp.user.profile p
            WHERE t.id = :teamId
            """)
    Page<TeamPostDto> getTeamPostsByTeamId(@Param("pageable") Pageable pageable,
            @Param("teamId") Long teamId);
}
