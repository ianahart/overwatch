package com.hart.overwatch.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.team.dto.TeamDto;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = """
            SELECT COUNT (t.id) AS totalTeams
            FROM Team t
            INNER JOIN t.user u
            WHERE u.id = :userId
            """)
    Long getTeamCountByUserId(@Param("userId") Long userId);

    @Query(value = """
             SELECT NEW com.hart.overwatch.team.dto.TeamDto(
             t.id AS id, u.id AS userId, t.teamName AS teamName,
             t.teamDescription AS teamDescription,
            (SELECT COUNT(t2.id) FROM Team t2 WHERE t2.user.id = :userId) AS totalTeams
             ) FROM Team t
             INNER JOIN t.user u
             WHERE u.id = :userId
             """)
    Page<TeamDto> getTeamsByUserId(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);

    @Query(value = """
                SELECT EXISTS(SELECT 1 FROM Team t
                INNER JOIN t.user u
                WHERE u.id = :userId
                AND LOWER(t.teamName) = :teamName
                )
            """)
    boolean existsByUserIdAndTeamName(@Param("userId") Long userId,
            @Param("teamName") String teamName);
}
