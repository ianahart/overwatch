package com.hart.overwatch.chatmessage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.chatmessage.dto.ChatMessageDto;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = """
                SELECT new com.hart.overwatch.chatmessage.dto.ChatMessageDto(
                cm.id AS id, u.firstName AS firstName, u.lastName AS lastName,
                cm.createdAt AS createdAt, cm.text AS text, up.avatarUrl AS avatarUrl,
                cn.id AS connectionId, u.id AS userId
                ) FROM ChatMessage cm
                INNER JOIN cm.user u
                INNER JOIN u.profile up
                INNER JOIN cm.connection cn
                WHERE cn.id = :connectionId
                ORDER BY cm.id DESC LIMIT 50
            """)
    List<ChatMessageDto> getChatMessages(@Param("connectionId") Long connectionId);

    @Query(value = """
                SELECT new com.hart.overwatch.chatmessage.dto.ChatMessageDto(
                cm.id AS id, u.firstName AS firstName, u.lastName AS lastName,
                cm.createdAt AS createdAt, cm.text AS text, up.avatarUrl AS avatarUrl,
                cn.id AS connectionId, u.id AS userId
                ) FROM ChatMessage cm
                INNER JOIN cm.user u
                INNER JOIN u.profile up
                INNER JOIN cm.connection cn
                WHERE cm.id = :chatMessageId
            """)

    ChatMessageDto getChatMessage(@Param("chatMessageId") Long chatMessageId);
}
