package com.hart.overwatch.teaminvitation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {

    @Query(value = """
            SELECT NEW com.hart.overwatch.teaminvitation.dto.TeamInvitationDto(
             ti.id AS id, s.id AS senderId, r.id AS receiverId, ti.status AS status,
             p.avatarUrl AS senderAvatarUrl, s.fullName AS senderFullName, t.teamName as teamName
            ) FROM TeamInvitation ti
            INNER JOIN ti.sender s
            INNER JOIN ti.receiver r
            INNER JOIN ti.sender.profile p
            INNER JOIN ti.team t
            WHERE r.id = :receiverId
            """)
    Page<TeamInvitationDto> getTeamInvitationsByReceiverId(@Param("pageable") Pageable pageable,
            @Param("receiverId") Long receiverId);

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
