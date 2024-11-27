package com.hart.overwatch.teaminvitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {

    @Query(value = """
                SELECT EXISTS(SELECT 1 FROM TeamInvitation ti
                INNER JOIN ti.receiver r
                INNER JOIN ti.sender s
                INNER JOIN ti.team t
                WHERE r.id = :receiverId
                AND s.id = :senderId
                AND t.id = :teamId
                )
            """)
    boolean existsByReceiverIdAndSenderIdAndTeamId(@Param("receiverId") Long receiverId,
            @Param("senderId") Long senderId, @Param("teamId") Long teamId);
}
