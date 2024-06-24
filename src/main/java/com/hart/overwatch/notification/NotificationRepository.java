package com.hart.overwatch.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.notification.dto.NotificationDto;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query(value = """
            SELECT new com.hart.overwatch.notification.dto.NotificationDto(
            n.id AS id, n.createdAt AS createdAt, n.text AS text,
            r.id AS receiverId, s.id AS senderId,
            CASE
                WHEN n.notificationRole = 'RECEIVER' THEN sp.avatarUrl
                WHEN n.notificationRole = 'SENDER' THEN rp.avatarUrl
                ELSE NULL
            END AS avatarUrl,
            n.notificationType AS notificationType,
            n.notificationRole AS notificationRole
            ) FROM Notification n
            INNER JOIN n.receiver r
            INNER JOIN n.sender s
            LEFT JOIN r.profile rp
            LEFT JOIN s.profile sp
            WHERE (s.id = :userId AND n.notificationRole = 'SENDER')
               OR (r.id = :userId AND n.notificationRole = 'RECEIVER')
               OR (r.id = :userId AND s.id = :userId)
            """)
    Page<NotificationDto> getAllNotificationsByReceiverId(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);
}
