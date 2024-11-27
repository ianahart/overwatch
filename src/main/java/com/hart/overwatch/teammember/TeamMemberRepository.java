package com.hart.overwatch.teammember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {


    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM TeamMember tm
                INNER JOIN tm.team t
                INNER JOIN tm.user u
                WHERE t.id = :teamId
                AND u.id = :userId
                )
            """)
    boolean existsByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
