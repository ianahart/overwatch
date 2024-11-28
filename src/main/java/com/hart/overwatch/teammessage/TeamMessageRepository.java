package com.hart.overwatch.teammessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teammessage.dto.TeamMessageDto;
import java.util.List;

@Repository
public interface TeamMessageRepository extends JpaRepository<TeamMessage, Long> {
    @Query(value = """
                SELECT new com.hart.overwatch.teammessage.dto.TeamMessageDto(
                 tm.id AS id, tm.text AS text, tm.createdAt AS createdAt,
                 u.id AS userId, u.fullName AS fullName, p.avatarUrl AS avatarUrl, t.id AS teamId
                ) FROM TeamMessage tm
                INNER JOIN tm.team t
                INNER JOIN tm.user u
                INNER JOIN tm.user.profile p
                WHERE t.id = :teamId
                ORDER BY tm.id DESC LIMIT 50
            """)
    List<TeamMessageDto> getTeamMessagesByTeamId(@Param("teamId") Long teamId);

}
