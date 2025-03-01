package com.hart.overwatch.teampinnedmessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamPinnedMessageRepository extends JpaRepository<TeamPinnedMessage, Long> {


    @Query(value = """
                SELECT COUNT(tpm.id) FROM TeamPinnedMessage tpm
                INNER JOIN tpm.team t
                WHERE t.id = :teamId
            """)
    long totalTeamPinnedMessages(@Param("teamId") Long teamId);
}
