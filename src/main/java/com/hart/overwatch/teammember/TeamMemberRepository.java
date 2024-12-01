package com.hart.overwatch.teammember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teammember.dto.TeamMemberDto;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findTeamMemberByTeamId(@Param("teamId") Long teamId);

    @Query(value = """
            SELECT COUNT(tm.id) FROM TeamMember tm
            INNER JOIN tm.user u
            WHERE u.id = :userId
            """)
    long countTeamMemberTeams(@Param("userId") Long userId);

    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM TeamMember tm
                INNER JOIN tm.team t
                INNER JOIN tm.user u
                WHERE t.id = :teamId
                AND u.id = :userId
                )
            """)
    boolean existsByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query(value = """
             SELECT new com.hart.overwatch.teammember.dto.TeamMemberTeamDto(
              tm.id AS id, u.id AS userId, t.id AS teamId, t.teamName AS teamName
             ) FROM TeamMember tm
             INNER JOIN tm.user u
             INNER JOIN tm.team t
             WHERE u.id = :userId
            """)
    Page<TeamMemberTeamDto> getTeamsByTeamMember(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);

    @Query(value = """
                SELECT new com.hart.overwatch.teammember.dto.TeamMemberDto(
                 tm.id AS id, u.id AS userId, t.id AS teamId, p.id AS profileId,
                 u.fullName AS fullName, p.avatarUrl AS avatarUrl, tm.createdAt AS createdAt
                ) FROM TeamMember tm
                INNER JOIN tm.user u
                INNER JOIN tm.user.profile p
                INNER JOIN tm.team t
                WHERE t.id = :teamId
            """)
    Page<TeamMemberDto> getTeamMembersByTeamId(@Param("pageable") Pageable pageable,
            @Param("teamId") Long teamId);


    @Query(value = """
                SELECT new com.hart.overwatch.teammember.dto.TeamMemberDto(
                 tm.id AS id, u.id AS userId, t.id AS teamId, p.id AS profileId,
                 u.fullName AS fullName, p.avatarUrl AS avatarUrl, tm.createdAt AS createdAt
                ) FROM TeamMember tm
                INNER JOIN tm.user u
                INNER JOIN tm.user.profile p
                INNER JOIN tm.team t
                WHERE t.id = :teamId
                AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<TeamMemberDto> searchTeamMembersByTeamId(@Param("pageable") Pageable pageable,
            @Param("teamId") Long teamId, @Param("search") String search);

}
