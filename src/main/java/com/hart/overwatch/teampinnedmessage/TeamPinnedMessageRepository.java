package com.hart.overwatch.teampinnedmessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto;


@Repository
public interface TeamPinnedMessageRepository extends JpaRepository<TeamPinnedMessage, Long> {


    @Query(value = """
                SELECT COUNT(tpm.id) FROM TeamPinnedMessage tpm
                INNER JOIN tpm.team t
                WHERE t.id = :teamId
            """)
    long totalTeamPinnedMessages(@Param("teamId") Long teamId);

    @Query(value = """
                SELECT NEW com.hart.overwatch.teampinnedmessage.dto.TeamPinnedMessageDto(
                 tpm.id AS id, u.id AS userId, u.fullName AS fullName, p.avatarUrl AS avatarUrl,
                 tpm.createdAt AS createdAt, tpm.message AS message, tpm.isEdited AS isEdited,
                 tpm.updatedAt AS updatedAt, tpm.index AS index
                ) FROM TeamPinnedMessage tpm
                INNER JOIN tpm.user u
                INNER JOIN tpm.user.profile p
                INNER JOIN tpm.team t
                WHERE t.id = :teamId
            """)
    Page<TeamPinnedMessageDto> getTeamPinnedMessagesByTeamId(@Param("teamId") Long teamId,
            @Param("pageable") Pageable pageable);
}
