package com.hart.overwatch.connection;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.connection.dto.ConnectionDto;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findBySenderIdAndReceiverId(Long senderId, Long receiverId);



    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, r.firstName AS firstName,
             r.lastName AS lastName, rp.avatarUrl AS avatarUrl, rp.email AS email,
             rl.city AS city, rl.country AS country, rp.contactNumber as phoneNumber,
             rp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN r.profile rp
            INNER JOIN r.location rl
            WHERE s.id = :userId
            AND c.status = 'ACCEPTED'
            AND (LOWER(r.firstName) LIKE :query OR LOWER(r.lastName) LIKE :query)
            AND (:blockedUserIds IS NULL OR r.id NOT IN :blockedUserIds)
                """)
    Page<ConnectionDto> getSearchSenderConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("query") String query,
            @Param("blockedUserIds") List<Long> blockedUserIds);


    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, s.firstName AS firstName,
             s.lastName AS lastName, sp.avatarUrl AS avatarUrl, sp.email AS email,
             sl.city AS city, sl.country AS country, sp.contactNumber as phoneNumber,
             sp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN s.profile sp
            INNER JOIN s.location sl
            WHERE r.id = :userId
            AND c.status = 'ACCEPTED'
            AND (LOWER(s.firstName) LIKE :query OR LOWER(s.lastName) LIKE :query)
            AND (:blockedUserIds IS NULL OR s.id NOT IN :blockedUserIds)
                """)
    Page<ConnectionDto> getSearchReceiverConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("query") String query,
            @Param("blockedUserIds") List<Long> blockedUserIds);



    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, r.firstName AS firstName,
             r.lastName AS lastName, rp.avatarUrl AS avatarUrl, rp.email AS email,
             rl.city AS city, rl.country AS country, rp.contactNumber as phoneNumber,
             rp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN r.profile rp
            INNER JOIN r.location rl
            WHERE s.id = :userId
            AND c.status = 'ACCEPTED'
            AND (:connectionPinIds IS NULL OR c.id NOT IN :connectionPinIds)
            AND (:blockedUserIds IS NULL OR r.id NOT IN :blockedUserIds)
                """)
    Page<ConnectionDto> getSenderConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("connectionPinIds") List<Long> connectionPinIds,
            @Param("blockedUserIds") List<Long> blockedUserIds);


    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, r.firstName AS firstName,
             r.lastName AS lastName, rp.avatarUrl AS avatarUrl, rp.email AS email,
             rl.city AS city, rl.country AS country, rp.contactNumber as phoneNumber,
             rp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN r.profile rp
            INNER JOIN r.location rl
            WHERE s.id = :userId
            AND c.status = 'ACCEPTED'
            AND (:blockedUserIds IS NULL OR r.id NOT IN :blockedUserIds)

                """)
    Page<ConnectionDto> getSenderConnectionsWithoutPins(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("blockedUserIds") List<Long> blockedUserIds);


    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, s.firstName AS firstName,
             s.lastName AS lastName, sp.avatarUrl AS avatarUrl, sp.email AS email,
             sl.city AS city, sl.country AS country, sp.contactNumber as phoneNumber,
             sp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN s.profile sp
            INNER JOIN s.location sl
            WHERE r.id = :userId
            AND c.status = 'ACCEPTED'
            AND (:connectionPinIds IS NULL OR c.id NOT IN :connectionPinIds)
            AND (:blockedUserIds IS NULL OR s.id NOT IN :blockedUserIds)
                """)
    Page<ConnectionDto> getReceiverConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("connectionPinIds") List<Long> connectionPinIds,
            @Param("blockedUserIds") List<Long> blockedUserIds);

    @Query(value = """
             SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
              c.id AS id, r.id AS receiverId, s.id AS senderId, s.firstName AS firstName,
              s.lastName AS lastName, sp.avatarUrl AS avatarUrl, sp.email AS email,
              sl.city AS city, sl.country AS country, sp.contactNumber as phoneNumber,
              sp.bio AS bio
             ) FROM Connection c
             INNER JOIN c.receiver r
             INNER JOIN c.sender s
             INNER JOIN s.profile sp
             INNER JOIN s.location sl
             WHERE r.id = :userId
             AND c.status = 'ACCEPTED'
            AND (:blockedUserIds IS NULL OR s.id NOT IN :blockedUserIds)
                 """)
    Page<ConnectionDto> getReceiverConnectionsWithoutPins(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("blockedUserIds") List<Long> blockedUserIds);



    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM Connection c
             INNER JOIN c.sender s
             INNER JOIN c.receiver r
             WHERE r.id = :receiverId
             AND  s.id = :senderId
            )
            """)
    boolean findExistingConnection(@Param("receiverId") Long receiverId,
            @Param("senderId") Long senderId);
}
